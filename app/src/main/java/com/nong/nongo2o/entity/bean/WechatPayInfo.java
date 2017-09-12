package com.nong.nongo2o.entity.bean;


import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * H5调起支付API
 */
public class WechatPayInfo
{
    
    // 每个字段具体的意思请查看API文档
    private String appId;
    
    private String nonceStr;
    
    private String timeStamp;
    
    private String packages;
    
    private String signType;
    
    private String paySign;
    

    
    public Map<String, Object> toMap()
    {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields)
        {
            Object obj;
            try
            {
                obj = field.get(this);
                if (obj != null)
                {
                    // 特殊处理java关键字
                    if ("packages".equals(field.getName()))
                    {
                        map.put("package", obj);
                    }
                    else
                    {
                        map.put(field.getName(), obj);
                    }
                    
                }
            }
            catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        return map;
    }
    
    public String getAppId()
    {
        return appId;
    }
    
    public void setAppId(String appId)
    {
        this.appId = appId;
    }
    
    public String getNonceStr()
    {
        return nonceStr;
    }
    
    public void setNonceStr(String nonceStr)
    {
        this.nonceStr = nonceStr;
    }
    
    public String getTimeStamp()
    {
        return timeStamp;
    }
    
    public void setTimeStamp(String timeStamp)
    {
        this.timeStamp = timeStamp;
    }
    
    public String getPackages()
    {
        return packages;
    }
    
    public void setPackages(String packages)
    {
        this.packages = packages;
    }
    
    public String getSignType()
    {
        return signType;
    }
    
    public void setSignType(String signType)
    {
        this.signType = signType;
    }
    
    public String getPaySign()
    {
        return paySign;
    }
    
    public void setPaySign(String paySign)
    {
        this.paySign = paySign;
    }
    
}
