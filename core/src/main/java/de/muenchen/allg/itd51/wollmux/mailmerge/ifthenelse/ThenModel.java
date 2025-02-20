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
package de.muenchen.allg.itd51.wollmux.mailmerge.ifthenelse;

import de.muenchen.allg.itd51.wollmux.config.ConfigThingy;
import de.muenchen.allg.itd51.wollmux.config.NodeNotFoundException;

/**
 * The model of a "THEN" node in an "IF" function.
 */
public class ThenModel extends IfThenElseBaseModel
{
  public ThenModel()
  {
  }

  /**
   * Create the model from a configuration.
   *
   * @param conf
   *          The configuration.
   */
  public ThenModel(ConfigThingy conf)
  {
    if (conf.count() == 1)
    {
      try
      {
        ConfigThingy innerConf = conf.getFirstChild();
        if (innerConf.count() == 0)
        {
          setValue("");
        } else if (innerConf.getName().equals("CAT"))
        {
          setValue(parseCatFunction(innerConf));
        }
      } catch (NodeNotFoundException e)
      {
        // Can't happen as we tested count before
      }
    }
  }

  @Override
  public String toString()
  {
    return "DANN " + getValue();
  }
}
