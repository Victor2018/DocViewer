/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package com.nvqquy98.lib.doc.office.fc.hwpf.usermodel;

import com.nvqquy98.lib.doc.office.fc.hwpf.model.types.TCAbstractType;
import com.nvqquy98.lib.doc.office.fc.util.LittleEndian;


public final class TableCellDescriptor extends TCAbstractType implements
        Cloneable
{
  public static final int SIZE = 20;

  protected  short field_x_unused;

  public TableCellDescriptor()
  {
    setBrcTop(new BorderCode());
    setBrcLeft(new BorderCode());
    setBrcBottom(new BorderCode());
    setBrcRight(new BorderCode());

  }

  protected void fillFields(byte[] data, int offset)
  {
    field_1_rgf = LittleEndian.getShort(data, 0x0 + offset);
    field_x_unused = LittleEndian.getShort(data, 0x2 + offset);
    setBrcTop(new BorderCode(data, 0x4 + offset));
    setBrcLeft(new BorderCode(data, 0x8 + offset));
    setBrcBottom(new BorderCode(data, 0xc + offset));
    setBrcRight(new BorderCode(data, 0x10 + offset));
  }

  public void serialize(byte[] data, int offset)
  {
      LittleEndian.putShort(data, 0x0 + offset, field_1_rgf);
      LittleEndian.putShort(data, 0x2 + offset, field_x_unused);
      getBrcTop().serialize(data, 0x4 + offset);
      getBrcLeft().serialize(data, 0x8 + offset);
      getBrcBottom().serialize(data, 0xc + offset);
      getBrcRight().serialize(data, 0x10 + offset);
  }

  public Object clone()
    throws CloneNotSupportedException
  {
    TableCellDescriptor tc = (TableCellDescriptor)super.clone();
    tc.setBrcTop((BorderCode)getBrcTop().clone());
    tc.setBrcLeft((BorderCode)getBrcLeft().clone());
    tc.setBrcBottom((BorderCode)getBrcBottom().clone());
    tc.setBrcRight((BorderCode)getBrcRight().clone());
    return tc;
  }

  public static TableCellDescriptor convertBytesToTC(byte[] buf, int offset)
  {
    TableCellDescriptor tc = new TableCellDescriptor();
    tc.fillFields(buf, offset);
    return tc;
  }

}
