package cn.orderMeal.common.kit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



/**
 * 日期工具类
 * @author gd_xbb
 */
public class DateKit {
	/**默认格式（2016-08-20）*/
	public static final String DEFAULT_FORMAT = "yyyy-MM-dd";
	/**详细日期格式（2016-08-20 17:30:30）*/
	public static final String DETAIL = "yyyy-MM-dd HH:mm:ss";
	/**带/的日期格式（2016/08/20）*/
	public static final String FORMAT_SLASH = "yyyy/MM/dd";
	/**中文的日期格式（2016年08月20日）*/
	public static final String CHINESE_FORMAT = "yyyy年MM月dd日";
	public static final String CHINESE_FORMAT_DETAIL = "yyyy年MM月dd日 HH:mm:ss";
	/*一天 时间毫秒数*/
	public static final long ONE_DAY_MILLIS = 1000*3600*24;
	
	/**
	 * 将Date类型转换为字符串类型 
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatDate(Date date, String format){
		if (null == date){
			return null;
		}
		if (StringKit.isEmpty(format)){
			format = DEFAULT_FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 将String类型转换为date类型
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String dateStr, String format) throws ParseException{
		if (StringKit.isEmpty(dateStr)){
			return null;
		}
		if (StringKit.isEmpty(format)){
			format = DEFAULT_FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(dateStr);
	}
	
	/**
	 * 得到本月最开始的时间
	 * 
	 * @return
	 */
	public static Date currentMonthStart(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 得到本月最后一刻时间
	 * @return
	 */
	public static Date currentMonthEnd(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	/**
	 * 得到本年年份
	 * @return
	 */
	public static int currentYear(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}
	
	/**
	 * 通过生日得到年龄
	 * @param birthDate
	 * @return
	 */
	public static int getAge(Date birthDate) {
		if (birthDate == null)
			throw new RuntimeException("出生日期不能为null");
		int age = 0;
		Date now = new Date();
		SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
		SimpleDateFormat format_M = new SimpleDateFormat("MM");
		String birth_year = format_y.format(birthDate);
		String this_year = format_y.format(now);
		String birth_month = format_M.format(birthDate);
		String this_month = format_M.format(now);
		// 初步，估算
		age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);
		// 如果未到出生月份，则age - 1
		if (this_month.compareTo(birth_month) < 0)
			age -= 1;
		if (age < 0)
			age = 0;
		return age;
	}
	
	
	/**
	 * 通过日期生成惟一主键
	 * @return
	 */
	public static synchronized String generateId(){
		return System.currentTimeMillis()+"";
	}
	
	
	public static synchronized String generateDateId(){
		Date date = new Date();
		String dateStr = formatDate(date, "yyyyMMddHHmmss");
		return dateStr;
	}

	public static void main(String args[]){
		System.out.println(generateDateId());
	}
	
}
