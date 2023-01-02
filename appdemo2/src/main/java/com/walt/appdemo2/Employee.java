package com.walt.appdemo2;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * description:
 * Author:zhongxj
 * Email:xianjian@nolovr.com
 * Date:2023/1/1
 */
public final class Employee implements BaseColumns {
    public static final String AUTHORITY = "com.walt.provider.Employee";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/employee");
    public static final Uri CONTENT_ACTION_URI = Uri.parse("content://" + AUTHORITY);

    public static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.walt.employees";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.walt.employees";

    public static final String DEFAULT_SORT_ORDER = "name DESC";

    public static final String NAME = "name";
    public static final String GENDER = "gender";
    public static final String AGE =  "age";

    private Employee(){}

}
