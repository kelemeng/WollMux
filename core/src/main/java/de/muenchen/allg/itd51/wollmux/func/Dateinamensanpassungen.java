/*-
 * #%L
 * WollMux
 * %%
 * Copyright (C) 2005 - 2022 Landeshauptstadt München
 * %%
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl5
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * #L%
 */
package de.muenchen.allg.itd51.wollmux.func;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.star.uno.AnyConverter;

import de.muenchen.allg.afid.UnoHelperException;
import de.muenchen.allg.util.UnoComponent;
import de.muenchen.allg.util.UnoProperty;

public class Dateinamensanpassungen
{
  private static final Logger LOGGER = LoggerFactory
      .getLogger(Dateinamensanpassungen.class);

  private static final Pattern PROP = Pattern.compile("\\$\\{([^\\}]+)\\}");

  private Dateinamensanpassungen()
  {
  }

  /**
   * Dieser Funktion kann eine durch Pipe ('|') getrennte Liste mit
   * Pfaden/Dateinamen übergeben werden, wovon der erste Eintrag dieser Liste
   * zurückgegeben wird, dessen Pfad-Anteil tatsächlich verfügbar ist.
   * Innerhalb eines Pfades/Dateinamens kann vor der Verfügbarkeitsprüfung mit
   * ${<name>} der Wert einer Java-Systemproperty in den Dateinamen eingefügt
   * werden.
   */
  public static String verfuegbarenPfadVerwenden(String fileName)
  {
    String[] paths = fileName.split("\\s*\\|\\s*");
    String first = null;
    for (String p : paths)
    {
      String replacePath = replaceProperties(p);

      if (first == null)
        first = replacePath;

      File f = new File(replacePath);
      File parent = f.getParentFile();
      if (parent != null && parent.isDirectory())
        return f.toString();
    }
    if (first == null)
      first = paths[0];
    return new File(first).getName();
  }

  /**
   * Arbeitet wie
   * {@link Dateinamensanpassungen#verfuegbarenPfadVerwenden(String)} und
   * nimmt zusätzlich die folgenden LHM-spezifischen Dateinamensanpassungen
   * vor:
   *
   * a. Substituiert werden ß in ss ä in ae ö in oe ü in ue, Ä in Ae, Ü in ue,
   * Ö in Oe
   *
   * b. Alle Sonderzeichen, Satzzeichen etc. sollen durch _ ersetzt werden,
   * außer dem Punkt vor der Dateiendung (.odt)
   *
   * c. Damit sind im Dateinamen nur noch die Zahlen von 0-9, die Buchstaben
   * von a-z und A-Z und der Unterstrich _ vorhanden
   *
   * d. Die Länge des Dateinamens wird auf maximal 240 Zeichen (inkl. Pfad)
   * begrenzt; ist der ermittelte Dateiname länger, so wird er nach 240
   * Zeichen abgeschnitten (genau genommen wird nach 236 Zeichen abgeschnitten
   * und dann wird die Endung .odt angehängt).
   *
   * Arbeitsverzeichnispfad in LibreOffice wird an Dateiname angehängt, falls spezifizierte Dateiname nicht absolut ist.
   */
  public static String lhmDateinamensanpassung(String fileName)
  {
    fileName = replaceProperties(fileName);
    File f = new File(fileName);
    if (!f.isAbsolute())
    {
      try
      {
        // holt den Arbeitsverzeichnispfad aus LO
        Object ps = UnoComponent.createComponentWithContext(UnoComponent.CSS_UTIL_PATH_SETTINGS);
        URL dir = new URL(AnyConverter.toString(UnoProperty.getProperty(ps, UnoProperty.WORK)));
        f = new File(dir.getPath(), fileName);
      } catch (MalformedURLException | UnoHelperException e)
      {
        LOGGER.error("", e);
      }
    }
    String pfad = verfuegbarenPfadVerwenden(f.getAbsolutePath());
    File file = new File(pfad);
    int parentLength = 0;
    if (file.getParent() != null)
      parentLength = file.getParent().length() + 1;

    String name = file.getName();
    String suffix = "";
    int idx = name.lastIndexOf('.');
    if (idx >= 0)
    {
      suffix = name.substring(idx);
      if (suffix.matches("\\.\\w{3,4}"))
        name = name.substring(0, idx);
      else
        suffix = "";
    }

    name = name.replaceAll("ß", "ss");
    name = name.replaceAll("ä", "ae");
    name = name.replaceAll("ö", "oe");
    name = name.replaceAll("ü", "ue");
    name = name.replaceAll("Ä", "Ae");
    name = name.replaceAll("Ö", "Oe");
    name = name.replaceAll("Ü", "Ue");
    name = name.replaceAll("[^a-zA-Z_0-9]", "_");

    int maxlength = 240 - suffix.length() - parentLength;
    if (name.length() > maxlength)
      name = name.substring(0, maxlength);

    name = name + suffix;

    file = new File(file.getParentFile(), name);
    return file.toString();
  }

  private static String replaceProperties(final String fileName)
  {
    // alle ${<prop>} durch evaluierten Inhalt ersetzen
    Matcher m = PROP.matcher(fileName);
    StringBuffer buf = new StringBuffer();
    while (m.find())
    {
      String propVal = System.getProperty(m.group(1).trim());
      if (propVal == null)
        propVal = "";
      m.appendReplacement(buf, Matcher.quoteReplacement(propVal));
    }
    m.appendTail(buf);
    return buf.toString();
  }
}
