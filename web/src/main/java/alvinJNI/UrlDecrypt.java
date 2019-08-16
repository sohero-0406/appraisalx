package alvinJNI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("serial")
public class UrlDecrypt {

    private String urlKey;
    private String urlVal;

    public static Map<String, String> map = new HashMap<String, String>();


    /**
     */
    static {
        map.put("firstUrl", "manageMain");
        map.put("secondUrl", "manageMain");
        map.put("thirdUrl", "manageMain");
        map.put("fourUrl", "manageMain");
        map.put("fiveUrl", "manageMain");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static int test(String urlKey1, Object o, Class c, HttpServletRequest request) {
        String urlVal1 = "";
        if (urlKey1 != null && !"".equals(urlKey1)) {
//			//System.out.println("进来了");
            if (RegisterUtil.registerTableExist("uniquezs", request.getSession().getServletContext())
                    && RegisterUtil.registerTableExist("RegInfozs", request.getSession().getServletContext())) {//正式注册
//				//System.out.println("正式注册");
                urlVal1 = RegisterUtil.urlValue(urlKey1, "zs", request.getSession().getServletContext());
            } else {//试用注册
//				//System.out.println("试用注册");
                urlVal1 = RegisterUtil.urlValue(urlKey1, "sy", request.getSession().getServletContext());
            }
        }
        try {
            urlVal1 = urlVal1.substring(0, 1).toLowerCase() + urlVal1.substring(1);
            Method m = c.getMethod(urlVal1);
            m.invoke(o);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return 2;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static String testString(String urlKey1, Object o, Class c, HttpServletRequest request) {

        String urlVal1 = "";
        String x = "-1";
        if (urlKey1 != null && !"".equals(urlKey1)) {
            //System.out.println("进来了");
            if (RegisterUtil.registerTableExist("uniquezs", request.getSession().getServletContext())
                    && RegisterUtil.registerTableExist("RegInfozs", request.getSession().getServletContext())) {//正式注册
                //System.out.println("正式注册");
                urlVal1 = RegisterUtil.urlValue(urlKey1, "zs", request.getSession().getServletContext());
            } else {//试用注册
                //System.out.println("试用注册");
                urlVal1 = RegisterUtil.urlValue(urlKey1, "sy", request.getSession().getServletContext());
            }
        }

        try {
            if (urlVal1 != "") {
                urlVal1 = urlVal1.substring(0, 1).toLowerCase() + urlVal1.substring(1);
                Method m = c.getMethod(urlVal1);
                Object obj = m.invoke(o);
                x = obj.toString();
            } else {
                x = "0";
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            x = "1";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            x = "1";
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            x = "1";
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            x = "1";
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            x = "1";
        }
        return x;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String test2(String urlKey1, Object o, Class c, HttpServletRequest request, Class<?>[] classes, Object[] obs) {
        String urlVal1 = "";
        if (map.size() > 0) {
            urlVal1 = map.get(urlKey1);
        } else {
            if (urlKey1 != null && !"".equals(urlKey1)) {
                if (RegisterUtil.registerTableExist("uniquezs", request.getSession().getServletContext())
                        && RegisterUtil.registerTableExist("RegInfozs", request.getSession().getServletContext())) {//正式注册
                    urlVal1 = RegisterUtil.urlValue(urlKey1, "zs", request.getSession().getServletContext());
                    //System.out.println("zs");
                } else {//试用注册
                    urlVal1 = RegisterUtil.urlValue(urlKey1, "sy", request.getSession().getServletContext());
                    //System.out.println("sy");
                }
            }
        }
        String re = null;
        try {
            urlVal1 = urlVal1.substring(0, 1).toLowerCase() + urlVal1.substring(1);
            if (classes != null && classes.length > 0) {
                Method m = c.getMethod(urlVal1, classes);
                re = m.invoke(o, obs).toString();
            } else {
                Method m = c.getMethod(urlVal1);
                re = m.invoke(o).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return re;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public String getUrlVal() {
        return urlVal;
    }

    public void setUrlVal(String urlVal) {
        this.urlVal = urlVal;
    }
}
