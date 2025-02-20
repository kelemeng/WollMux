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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import de.muenchen.allg.itd51.wollmux.config.ConfigThingy;
import de.muenchen.allg.itd51.wollmux.config.ConfigurationErrorException;
import de.muenchen.allg.itd51.wollmux.dialog.DialogLibrary;

public class BindFunctionTest
{

  @Test
  public void testBindFunction() throws Exception
  {
    FunctionLibrary funcLib = new FunctionLibrary();
    DialogLibrary dialogLib = new DialogLibrary();
    HashMap<Object, Object> context = new HashMap<>();
    
    Function f = new BindFunction(new ValueFunction("test"), new ConfigThingy("", "SET(\"test\" \"value\")"), funcLib,
        dialogLib, context);
    assertEquals(0, f.parameters().length);
    Collection<String> dialogFunctions = new ArrayList<>();
    f.getFunctionDialogReferences(dialogFunctions);
    assertTrue(dialogFunctions.isEmpty());
    assertFalse(f.getBoolean(null));
    assertEquals("value", f.getResult(null));

    f = new BindFunction(new ValueFunction("test"), new ConfigThingy("", ""), funcLib, dialogLib, context);
    Values.SimpleMap values = new Values.SimpleMap();
    values.put("test", "value2");
    assertEquals(1, f.parameters().length);
    assertFalse(f.getBoolean(values));
    assertEquals("value2", f.getResult(values));

    f = new BindFunction(new ValueFunction("test"), new ConfigThingy("", "SET(\"test\" VALUE(\"test2\"))"), funcLib,
        dialogLib, context);
    values = new Values.SimpleMap();
    values.put("test2", FunctionLibrary.ERROR);
    assertFalse(f.getBoolean(values));
    assertEquals(FunctionLibrary.ERROR, f.getResult(values));

    assertThrows(ConfigurationErrorException.class, () -> new BindFunction(new ValueFunction("test"),
        new ConfigThingy("", "SET(\"test\" \"value\") SET(\"test\" \"value\")"), funcLib, dialogLib, context));

    assertThrows(ConfigurationErrorException.class, () -> new BindFunction(new ValueFunction("test"),
        new ConfigThingy("", "SET(\"test\")"), funcLib, dialogLib, context));
  }

}
