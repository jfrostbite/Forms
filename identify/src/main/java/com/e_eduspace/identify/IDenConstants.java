package com.e_eduspace.identify;

import com.e_eduspace.identify.singleLineWidget.WidgetExpress;

/**
 * Created by Administrator on 2017-06-06.
 * 表单按区块进行计算 tag
 * 区块内标记 未知 loc
 */

public interface IDenConstants {

    /**
     * 第一页表单资料,参与识别
     */
    String APPLY_FORM_ONE[] = {"apply_product","basic_info","profession_info","contacts_info"};
    //子 内容
    String APPLY_PRODUCT[] = {"no","product_no","product_name","member_no"};
    Integer APPLY_PRODUCT_TYPES[] = {WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_DIGIT};
    String BASIC_INFO[] = {"name_cn","name_en","birth_year","birth_month","birth_day","id_no","id_office","id_usable_year","id_usable_month","id_usable_day","birth_province","birth_city","nationality","primary_school",
    "phone_num","residential_province","residential_city","residential_street","residential_tel_zone","residential_tel_num","residential_postcode","checkin_date_year","checkin_date_month","checkin_date_day",
    "card_info_few","card_info_idnum","card_info_money"};
    Integer BASIC_INFO_TYPES[] = {WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_EN,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_TEXT,
            WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,
            WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT};
    String PROFESSION_INFO[] = {"company_name","compay_address_province","company_address_city","company_address_street","company_postcode","company_tel_zone","company_tel_num","company_tel_extension","entry_date_year","entry_date_month","annual_income"};
    Integer PROFESSION_INFO_TYPES[] = {WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT};
    String CONTACTS_INFO[] = {"kinsfolk_name","kinsfolk_phone_num","kinsfolk_tel_zone","kinsfolk_tel_num","kinsfolk_tel_extension"
    ,"other_name","other_phone_num","other_tel_zone","other_tel_num","other_tel_extension"};
    Integer CONTACTS_INFO_TYPES[] = {WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT
    ,WidgetExpress.IDENTIFY_TYPE_TEXT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT,WidgetExpress.IDENTIFY_TYPE_DIGIT};

    /**
     * 第一页个区块数
     */
    int APPLY_PRODUCT_SIZE = 4;
    int BASIC_INFO_SIZE = 27;
    int PROFESSION_INFO_SIZE = 11;
    int CONTACTS_INFO_SIZE = 10;

    int ONE_AREA_SIZE[] = {APPLY_PRODUCT_SIZE,BASIC_INFO_SIZE,PROFESSION_INFO_SIZE,CONTACTS_INFO_SIZE};
    String[] AREA_ONE_ELEMENT[] = {APPLY_PRODUCT,BASIC_INFO,PROFESSION_INFO,CONTACTS_INFO};
    Integer[] AREA_ONE_ELEMENT_TYPE[] = {APPLY_PRODUCT_TYPES,BASIC_INFO_TYPES,PROFESSION_INFO_TYPES,CONTACTS_INFO_TYPES};


    /**
     * 不参与识别，表示选择区域即可
     */
    String APPLY_FORM_ONE_CHOICE[] = {"sex","id_type","id_usable_long","marital_status","edu_status","residential_status","company_nature","belongs_trade","profession","position","kinship","other_relation"};

    /**
     * 所有选择数
     */
    int APPLY_FORM_ONE_CHOICE_SIZE = 15;

    /**
     * 第二页表单资料
     */
    String APPLY_FORM_TWO[] = {"vice_apply_info","single_server"};
    //子 内容
    String VICE_APPLY_INFO[] = {};
    String SINGLE_SERVER[] = {};
    /**
     * 第三页表单资料
     */
    String APPLY_FORM_THR[] = {"apply_product","basic_info","profession_info","contacts_info"};

//    Map<String,List<String>> generate();
}
