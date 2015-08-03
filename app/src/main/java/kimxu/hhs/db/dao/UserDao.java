/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kimxu.hhs.db.dao;

import android.content.Context;

import kimxu.hhs.db.DatabaseHelper;

public class UserDao {
    public static final String TABLE_NAME = "uers";
    public static final String COLUMN_NAME_ID = "username";
    public static final String COLUMN_NAME_NICK = "nick";
    //用户昵称
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_PORTRAIT = "portrait";//头像地址
    private DatabaseHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }


}
