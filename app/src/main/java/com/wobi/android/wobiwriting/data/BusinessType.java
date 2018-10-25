package com.wobi.android.wobiwriting.data;

/**
 * Created by wangyingren on 2017/9/22.
 */

/* 业务请求类型 */
public enum BusinessType {

    BT_ConnManager(0),                             /* 连接管理服务器,获取业务服务器地址 */
    BT_Get_Province(1),                                    /* 获取所有省 */
    BT_Get_City(2),                                            /* 根据省获取市 */
    BT_Get_Area(3),                                            /*  根据省市获取区 */
    BT_Get_Grade(4),                                       /* 获取所有年级信息 */
    BT_User_Verify_Phone(5),                           /*  用户注册 – 验证用户手机号码可用性 */
    BT_User_Get_Verify_Code(6),                    /* 用户注册 - 用户获取手机验证码 */
    BT_User_Commit_Register_Info(7),           /* 用户注册 - 用户提交注册信息 */
    BT_User_Login(8),                                      /*  用户登录 */
    BT_User_Get_Info(9),                                   /*  获取用户信息 */
    BT_User_Change_Info(10),                            /*  修改资料 */
    BT_User_Change_Password(11),                    /*  修改密码 */ //当前版本1.0.0不实现
    BT_User_Change_Phone(12),                       /*  更换手机号码 */ //当前版本1.0.0不实现
    BT_User_Logout(13),                                     /*  用户退出 */
    BT_Get_JC_List(14),                                             /* 获取教材列表 */
    BT_Get_KWML_List(15),                                        /* 根据教材获取课文目录列表 */
    BT_Get_SZ_List(16),                                              /* 根据课文目录获取生字列表 */
    BT_Get_SZ_Info(17),                                     /*根据生字获取生字信息 */
    BT_Shu_Fa_Ke_Tang_YB(18),                       /*  书法课堂硬笔 */
    BT_Shu_Fa_Ke_Tang_MB(19),                      /*  书法课堂毛笔 */
    BT_Di_Zi_Gui(20),                                           /* 国学经典 – 弟子规 */
    BT_San_Zi_Jing(21),                                     /*  国学经典 – 三字经 */
    BT_Search_Community_By_Name(22),    /*  根据圈子名称搜索圈子 */
    BT_Search_Owned_Community(23),          /*  搜索用户创建的圈子 */
    BT_Search_Joined_Community(24),         /*  搜索用户加入的圈子 */
    BT_Create_Community(25),                        /*  创建圈子 */
    BT_Change_Community_Info(26),               /*  修改圈子 */
    BT_Forced_Quit_Community(27),           /*  强制退出圈子 */
    BT_Comment_Community(28),                   /*  给圈子留言 */
    BT_Join_Community(29),                          /*  加入圈子 */
    BT_Quit_Community(30),                          /*  主动退出圈子 */
    BT_Buy_Single_Video(31),                        /*  购买单个视频 */
    BT_Buy_VIP_Service(32),                             /*  购买VIP服务 */
    BT_Zi_Dong_Ti_Xian(33),                              /*  用户提现 */
    BT_Upload_GPS_Pos(34),                               /*  上传GPS位置 */
    BT_Search_Community_By_Request_Code(35),      /* 根据邀请码搜索圈子 */
    BT_Search_Popular_Community(36),	          /* 搜索热门圈子 */
    BT_Search_Community_By_Keyword(37),          /* 跟据关键字搜索圈子,关键字是圈子名或邀请码 */
    BT_Get_WXPay_Result(38),			/* 获取微信支付的支付结果 */
    BT_Get_Province_City_Area(39),			/* 获取所有省市区 */
    BT_Get_Sc_Info(40),                     /* 获取生词信息 */
    BT_GET_VIP_LIST(41),                    /* 获取vip 套餐列表 */
    BT_Join_Community_By_RequestCode(44),			/* 根据邀请码加入圈子 */
    BT_Check_App_Upgrade(45),			/* 获取应用最新版本号 */
    BT_Gu_Shi(48);			/* 古诗诵读 */


    private int mValue;

    private BusinessType(int value){
        mValue = value;
    }

    public int getValue(){
        return mValue;
    }
}
