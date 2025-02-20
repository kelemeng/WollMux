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
package de.muenchen.allg.itd51.wollmux.form.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import de.muenchen.allg.itd51.wollmux.config.ConfigThingy;

public class TestTabConfig
{

  @Test
  public void testTabConfig() throws Exception
  {
    ConfigThingy conf = new ConfigThingy("Tab",
        "ID \"id\" TITLE \"title\" TIP \"tip\" HOTKEY \"key\" CLOSEACTION \"action\"");
    ConfigThingy inputConf = new ConfigThingy("Eingabefelder", "(TYPE \"LABEL\")");
    conf.addChild(inputConf);
    ConfigThingy buttonConf = new ConfigThingy("Buttons", "(TYPE \"LABEL\")");
    conf.addChild(buttonConf);
    TabConfig tab = new TabConfig(conf);
    assertEquals("Tab", tab.getId());
    assertEquals("tip", tab.getTip());
    assertEquals('K', tab.getHotkey());
    assertEquals("title", tab.getTitle());
    assertEquals("action", tab.getCloseAction());
    assertFalse(tab.getControls().isEmpty());
    assertFalse(tab.getButtons().isEmpty());
  }

}
