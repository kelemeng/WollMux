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
package de.muenchen.allg.itd51.wollmux.mailmerge.sidebar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Table;
import com.sun.star.util.XCloseListener;

import de.muenchen.allg.itd51.wollmux.config.ConfigThingy;
import de.muenchen.allg.itd51.wollmux.mailmerge.FieldSubstitution;
import de.muenchen.allg.itd51.wollmux.mailmerge.NoTableSelectedException;
import de.muenchen.allg.itd51.wollmux.mailmerge.ds.DatasourceModel;
import de.muenchen.allg.itd51.wollmux.mailmerge.ds.DatasourceModelListener;

public class MockDatasourceModel implements DatasourceModel
{
  @Override
  public int getNumberOfRecords() throws NoTableSelectedException
  {
    return 5;
  }

  @Override
  public Map<String, String> getRecord(int recordId) throws NoTableSelectedException
  {
    Map<String, String> map = new HashMap<>();
    map.put("Id", "" + recordId);
    return map;
  }

  @Override
  public void addCloseListener(XCloseListener arg0)
  {
  }

  @Override
  public void removeCloseListener(XCloseListener arg0)
  {
  }

  @Override
  public void addDatasourceListener(DatasourceModelListener listener)
  {
  }

  @Override
  public void removeDatasourceListener(DatasourceModelListener listener)
  {
  }

  @Override
  public String getName()
  {
    return null;
  }

  @Override
  public List<String> getTableNames()
  {
    return null;
  }

  @Override
  public void activateTable(String tableName) throws NoTableSelectedException
  {
  }

  @Override
  public String getActivatedTable() throws NoTableSelectedException
  {
    return null;
  }

  @Override
  public Set<String> getColumnNames() throws NoTableSelectedException
  {
    return null;
  }

  @Override
  public boolean supportsAddColumns()
  {
    return false;
  }

  @Override
  public void addColumns(Map<String, FieldSubstitution> mapIdToSubstitution)
      throws NoTableSelectedException
  {
  }

  @Override
  public Table<Integer, String, String> getData() throws NoTableSelectedException
  {
    return null;
  }

  @Override
  public void toFront()
  {
  }

  @Override
  public ConfigThingy getSettings() throws NoTableSelectedException
  {
    return null;
  }

  @Override
  public void dispose()
  {
  }
}
