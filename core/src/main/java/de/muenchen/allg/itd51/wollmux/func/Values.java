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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Eine Menge benannter {@link de.muenchen.allg.itd51.wollmux.func.Value}s.
 *
 * @author Matthias Benkmann (D-III-ITD 5.1)
 */
public interface Values
{
  /**
   * Liefert true genau dann wenn ein Wert mit der ID id vorhanden ist (ACHTUNG, bei
   * mit BIND zusammengesetzten Funktionen bekommt die gebundene Funktion unter
   * Umständen hier keine akkurate Antwort).
   */
  public boolean hasValue(String id);

  /**
   * Der aktuelle Wert des mit id identifizierten Values als String. Falls es sich um
   * einen booleschen Wert handelt, wird der String "true" oder "false"
   * zurückgeliefert. Falls kein Wert mit dieser id vorhanden ist wird der leere
   * String geliefert.
   */
  public String getString(String id);

  /**
   * Der aktuelle Wert des mit id identifizierten Values als boolean. Falls der Wert
   * seiner Natur nach ein String ist, so ist das Ergebnis implementierungsabhängig.
   * Falls kein Wert mit dieser id vorhanden ist wird false geliefert.
   */
  public boolean getBoolean(String id);

  /**
   * Dummy-Klasse, die ein Values-Interface zur Verfügung stellt, das keine Werte
   * enthält.
   */
  public static class None implements Values
  {
    @Override
    public boolean hasValue(String id)
    {
      return false;
    }

    @Override
    public String getString(String id)
    {
      return "";
    }

    @Override
    public boolean getBoolean(String id)
    {
      return false;
    }
  }

  /**
   * Simple Implementierung des Values-Interfaces in der Art einer Map.
   *
   * @author Matthias Benkmann (D-III-ITD 5.1)
   */
  public static class SimpleMap implements Values, Iterable<Map.Entry<String, String>>
  {
    private final HashMap<String, String> values;

    public SimpleMap()
    {
      values = new HashMap<>();
    }

    public SimpleMap(SimpleMap origin)
    {
      this.values = new HashMap<>(origin.values);
    }

    /**
     * Fügt den Wert value hinzu, identifiziert mit id. Ein bereits vorhandener Wert
     * wird ersetzt. Falls value==null, so wird der Aufruf behandelt wie
     * {@link #remove(String)}.
     */
    public void put(String id, String value)
    {
      if (value == null)
        remove(id);
      else
        values.put(id, value);
    }

    /**
     * Entfernt den Wert, der durch id identifiziert wird (falls vorhanden).
     */
    public void remove(String id)
    {
      values.remove(id);
    }

    /**
     * Fügt alle Werte aus der anderen SimpleMap dieser hinzu.
     *
     * @param map
     *          Die andere SimpleMap, aus der die Werte übernommen werden sollen.
     */
    public void putAll(SimpleMap map)
    {
      values.putAll(map.values);
    }

    @Override
    public boolean hasValue(String id)
    {
      return values.containsKey(id);
    }

    @Override
    public String getString(String id)
    {
      String str = values.get(id);
      if (str == null) {
        return "";
      }
      return str;
    }

    @Override
    public boolean getBoolean(String id)
    {
      return "true".equalsIgnoreCase(getString(id));
    }

    @Override
    public Iterator<Entry<String, String>> iterator()
    {
      return values.entrySet().iterator();
    }
  }
}
