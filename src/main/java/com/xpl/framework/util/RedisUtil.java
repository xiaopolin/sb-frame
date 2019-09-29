package com.xpl.framework.util;

import java.util.*;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
public class RedisUtil {

	@Autowired
	private RedisTemplate redisTemplate;

	private static final int TRUE = 0;
	private static final int FALSE = -10;

	/**
	 * 指定缓存失效时间
	 * 
	 * @param key  键
	 * @param time 时间(秒) time<0时设置key为永久
	 * @return
	 */
	public int expire(String key, long time) {
		if (null == key){
			log.error("redis-expire-error:The value of key is NULL");
			return FALSE;
		}
		boolean redisResult;
		try {
			if (time < 0){
				redisResult = redisTemplate.persist(key);
			}else {
				redisResult = redisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
			if (!redisResult){
				log.error("redis-expire-error:key does not exist");
			}
			return redisResult ? TRUE : FALSE;
		} catch (Exception e) {
			log.error("redis-expire-error:" + e.getMessage());
			return FALSE;
		}
	}

	/**
	 * 根据key 获取过期时间
	 * 
	 * @param key 键 不能为null
	 * @return 时间(秒) -1 key永久存在，即没有过期时间 -2 key不存在
	 */
	public long getExpire(String key) {
		if (null == key){
			log.error("redis-getExpire-error:The value of key is NULL");
			return FALSE;
		}
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	 * 判断key是否存在
	 * 
	 * @param key 键
	 * @return 0 存在 1 不存在
	 */
	public int hasKey(String key) {
		if (null == key){
			log.error("redis-hasKey-error:The value of key is NULL");
			return FALSE;
		}
		boolean hasKeyResult;
		try {
			hasKeyResult =  redisTemplate.hasKey(key);
		} catch (Exception e) {
			log.error("redis-hasKey-error:" + e.getMessage());
			return FALSE;
		}
		return hasKeyResult ? TRUE : 1;
	}

	/**
	 * 删除缓存
	 * 
	 * @param key 可以传一个值 或多个
	 */
	public int del(String... key) {
		if (key == null || key.length == 0){
			return FALSE;
		}
		try {
			if (key.length == 1) {
				redisTemplate.delete(key[0]);
			} else {
				redisTemplate.delete(CollectionUtils.arrayToList(key));
			}
		}catch (Exception e){
			log.error("redis-del-error:" + e.getMessage());
			return FALSE;
		}
		return TRUE;
	}

	//TODO VALUE
	/**
	 * 普通缓存获取
	 * 
	 * @param key 键
	 * @return 值
	 */
	public String get(String key) {
		if (key == null){
			return null;
		}
		try {
			return String.valueOf(redisTemplate.opsForValue().get(key));
		}catch (Exception e){
			log.error("redis-get-error:" + e.getMessage());
			return null;
		}
	}

	/**
	 * 普通缓存放入
	 * 
	 * @param key   键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public int set(String key, Object value) {
		if (null == key){
			return FALSE;
		}
		try {
			redisTemplate.opsForValue().set(key, value);
			return TRUE;
		} catch (Exception e) {
			log.error("redis-set-error:" + e.getMessage());
			return FALSE;
		}
	}

	/**
	 * 普通缓存放入并设置时间
	 * 
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public int set(String key, Object value, long time) {
		if (null == key){
			return FALSE;
		}
		try {
			if (time > 0) {
				redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
				return TRUE;
			} else {
				return set(key, value);
			}
		} catch (Exception e) {
			log.error("redis-set-error:" + e.getMessage());
			return FALSE;
		}
	}

	/**
	 * 递增
	 * 
	 * @param key 键
	 * @param increment 增加数量（不支持double的原因是存入redis后会自动补齐小数点后15位, 负数为+1）
	 * @return
	 */
	public long incr(String key, long increment) {

		if (null == key){
			log.error("redis-incr-error:The value of key is NULL");
			return FALSE;
		}
		try {
			if (0 < increment){
				return redisTemplate.opsForValue().increment(key);
			}else {
				return redisTemplate.opsForValue().increment(key, increment);
			}
		}catch (Exception e){
			log.error("redis-incr-error:" + e.getMessage());
			return FALSE;
		}
	}

	/**
	 * 递减
	 * 
	 * @param key 键
	 * @param decrement  要减少几
	 * @return
	 */
	public long decr(String key, long decrement) {
		if (null == key){
			log.error("redis-decr-error:The value of key is NULL");
			return FALSE;
		}
		try {
			if (0 < decrement){
				return redisTemplate.opsForValue().decrement(key);
			}else {
				return redisTemplate.opsForValue().decrement(key, decrement);
			}
		}catch (Exception e){
			log.error("redis-decr-error:" + e.getMessage());
			return FALSE;
		}
	}

	//TODO HAST
	/**
	 * HashPut
	 *
	 * @param key  键 不能为null
	 * @param title 项 不能为null
	 * @param value 项
	 * @return 值
	 */
	public int hPut(String key, String title, String value) {
		if (null == key || null == title){
			log.error("redis-hPut-error:The value of key/title is NULL");
			return FALSE;
		}
		try {
			redisTemplate.opsForHash().put(key, title, value);
		}catch (Exception e){
			log.error("redis-hPut-error:" + e.getMessage());
			return FALSE;
		}
		return TRUE;
	}

	/**
	 * HashPut
	 *
	 * @param key  键 不能为null
	 * @param tv 项 不能为null
	 * @return 值
	 */
	public int hPutAll(String key, Map<String, Object> tv) {
		if (null == key || tv.containsKey(null)){
			log.error("redis-hPutAll-error:The value of key/tv.key is NULL");
			return FALSE;
		}
		try {
			redisTemplate.opsForHash().putAll(key, tv);
		}catch (Exception e){
			log.error("redis-hPutAll-error:" + e.getMessage());
			return FALSE;
		}
		return TRUE;
	}

	/**
	 * HashPut 并设置时间
	 *
	 * @param key 不能为null
	 * @param tv  中的key不能为null
	 * @param time 过期时间
	 * @return 值
	 */
	public int hPutAll(String key, Map<String, Object> tv, long time) {
		return 0 == hPutAll(key, tv) ? expire(key, time) : FALSE;
	}

	/**
	 * HashGet
	 * 
	 * @param key  键 不能为null
	 * @param title 项 不能为null
	 * @return 值
	 */
	public String hGet(String key, String title) {
		if (null == key || null == title){
			log.error("redis-hGet-error:The value of key/title is NULL");
			return  null;
		}
		try {
			Object result =  redisTemplate.opsForHash().get(key, key);
			return null == result ? null : String.valueOf(result);
		}catch (Exception e){
			log.error("redis-hGet-error:" + e.getMessage());
			return null;
		}
	}

	/**
	 * 获取hashKey对应的所有键值
	 * 
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public Map<String, Object> hEntries(String key) {
		if (null == key){
			log.error("redis-hEntries-error:The value of key is NULL");
			return null;
		}
		try {
			return redisTemplate.opsForHash().entries(key);
		}catch (Exception e){
			log.error("redis-hEntries-error:" + e.getMessage());
			return null;
		}
	}

	/**
	 * 删除hash表中的值
	 * 
	 * @param key  键 不能为null
	 * @param field 项 可以使多个 不能为null
	 */
	public int hDelete(String key, Object... field) {
		if (null == key || null == field || Arrays.asList(field).contains(null)){
			log.error("redis-hDelete-error:The value of key/field is NULL");
			return FALSE;
		}
		try {
			redisTemplate.opsForHash().delete(key, field);
			return TRUE;
		}catch (Exception e){
			log.error("redis-hDelete-error:" + e.getMessage());
			return FALSE;
		}
	}

	/**
	 * 判断hash表中是否有该项的值
	 * 
	 * @param key  键 不能为null
	 * @param field 项 不能为null
	 * @return true 存在 false不存在
	 */
	public int hExists(String key, String field) {
		if (null == key || null == field){
			log.error("redis-hExists-error:The value of key/field is NULL");
			return FALSE;
		}
		try {
			return redisTemplate.opsForHash().hasKey(key, field) ? TRUE : FALSE;
		}catch (Exception e){
			log.error("redis-hExists-error:" + e.getMessage());
			return FALSE;
		}
	}

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 * 
	 * @param key  键
	 * @param field 项
	 * @param increment 增加数据
	 * @return
	 */
	public long hincr(String key, String field, long increment) {
		if (null == key || null == field){
			log.error("redis-hincr-error:The value of key/field is NULL");
			return FALSE;
		}
		try {
			return redisTemplate.opsForHash().increment(key, field, increment);
		}catch (Exception e){
			log.error("redis-hincr-error:" + e.getMessage());
			return FALSE;
		}
	}

	//TODO SET
	/**
	 * 将数据放入set缓存
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public int sAdd(String key, Object... values) {
		if (null == key || null == values || Arrays.asList(values).contains(null)){
			log.error("redis-sAdd-error:The value of key/value is NULL");
			return FALSE;
		}
		try {
			redisTemplate.opsForSet().add(key, values);
			return TRUE;
		} catch (Exception e) {
			log.error("redis-sAdd-error:" + e.getMessage());
			return FALSE;
		}
	}

	/**
	 * 将数据放入set缓存
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public int sAddWithExpire(String key, long time, Object... values) {
		return 0 == sAdd(key, values) ? expire(key, time) : FALSE;
	}

	/**
	 * 根据value从一个set中查询,是否存在
	 *
	 * @param key   键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public int sIsMenber(String key, Object value) {
		if (null == key || null == value){
			log.error("redis-sIsMenber-error:The value of key/value is NULL");
			return FALSE;
		}
		try {
			return redisTemplate.opsForSet().isMember(key, value) ? TRUE : FALSE;
		} catch (Exception e) {
			log.error("redis-sIsMenber-error:" + e.getMessage());
			return FALSE;
		}
	}

	/**
	 * 根据key获取Set中的所有值
	 * 
	 * @param key 键
	 * @return
	 */
	public Object[] sMembers(String key) {
		if (null == key){
			log.error("redis-sMembers-error:The value of key is NULL");
			return null;
		}
		try {
			return redisTemplate.opsForSet().members(key).toArray();
		} catch (Exception e) {
			log.error("redis-sMembers-error:" + e.getMessage());
			return null;
		}
	}

	/**
	 * 获取set缓存的长度
	 * 
	 * @param key 键
	 * @return
	 */
	public long sSize(String key) {
		if (null == key){
			log.error("redis-sSize-error:The value of key is NULL");
			return FALSE;
		}
		try {
			return redisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			log.error("redis-sSize-error:" + e.getMessage());
			return FALSE;
		}
	}

	/**
	 * 移除值为value的
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public long sRemove(String key, Object... value) {
		if (null == key || null == value || Arrays.asList(value).contains(null)){
			log.error("redis-sRemove-error:The value of key/value is NULL");
			return FALSE;
		}
		try {
			redisTemplate.opsForSet().remove(key, value);
			return TRUE;
		} catch (Exception e) {
			log.error("redis-sRemove-error:" + e.getMessage());
			return FALSE;
		}
	}

	//TODO LIST
	/**
	 * 将list放入缓存
	 *
	 * @param key 键
	 * @param type 1 为左插入 2（其余） 为右插入
	 * @param time 时间(秒)
	 * @param value 值
	 * @return
	 */
	public int lPush(String key, int type, long time, Object... value) {
		if (null == key || null == value || Arrays.asList(value).contains(null)){
			log.error("redis-lRightPush-error:The value of key/value is NULL");
			return FALSE;
		}
		try {
			if (1 == type){
				redisTemplate.opsForList().leftPush(key, value);
			}else {
				redisTemplate.opsForList().rightPush(key, value);
			}
			return expire(key, time);
		} catch (Exception e) {
			log.error("redis-lRightPush-error:" + e.getMessage());
			return FALSE;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key 键
	 * @param type 1 为左插入 2（其余） 为右插入
	 * @param time 时间(秒)
	 * @param value 值
	 * @return
	 */
	public int lPush(String key, int type, long time, Collection value) {
		if (null == key || null == value || value.contains(null)){
			log.error("redis-lRightPush-error:The value of key/value is NULL");
			return FALSE;
		}
		try {
			if (1 == type){
				redisTemplate.opsForList().leftPushAll(key, value);
			}else {
				redisTemplate.opsForList().rightPushAll(key, value);
			}
			return expire(key, time);
		} catch (Exception e) {
			log.error("redis-lRightPush-error:" + e.getMessage());
			return FALSE;
		}
	}

	/**
	 * 根据索引获取list缓存的内容
	 *
	 * @param key   键
	 * @param index 索引 index为正数时，从左往右数；负数时，从右往左数
	 * @return
	 */
	public Object lIndex(String key, long index) {
		if (null == key){
			log.error("redis-lIndex-error:The value of key is NULL");
			return null;
		}
		try {
			return redisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			log.error("redis-lIndex-error:" + e.getMessage());
			return null;
		}
	}

	/**
	 * 获取list缓存的内容
	 * 
	 * @param key   键
	 * @param start 开始
	 * @param end   结束 0 到 -1代表所有值
	 * @return
	 */
	public List<Object> lRange(String key, long start, long end) {
		if (null == key){
			log.error("redis-lRange-error:The value of key is NULL");
			return null;
		}
		try {
			return redisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			log.error("redis-lRange-error:" + e.getMessage());
			return null;
		}
	}

	/**
	 * 获取list缓存的长度
	 * 
	 * @param key 键
	 * @return
	 */
	public long lSize(String key) {
		if (null == key){
			log.error("redis-lSize-error:The value of key is NULL");
			return FALSE;
		}
		try {
			return redisTemplate.opsForList().size(key);
		} catch (Exception e) {
			log.error("redis-lSize-error:" + e.getMessage());
			return FALSE;
		}
	}

	/**
	 * 根据索引修改list中的某条数据
	 * 
	 * @param key   键
	 * @param index 索引
	 * @param value 值
	 * @return
	 */
	public int lSet(String key, long index, Object value) {
		if (null == key){
			log.error("redis-lSet-error:The value of key is NULL");
			return FALSE;
		}
		try {
			redisTemplate.opsForList().set(key, index, value);
			return TRUE;
		} catch (Exception e) {
			log.error("redis-lSet-error:" + e.getMessage());
			return FALSE;
		}
	}

	/**
	 * 移除N个值为value
	 * 
	 * @param key   键
	 * @param count 移除多少个
	 * @param value 值
	 * @return 移除的个数
	 */
	public long lRemove(String key, long count, Object value) {
		if (null == key){
			log.error("redis-lRemove-error:The value of key is NULL");
			return FALSE;
		}
		try {
			redisTemplate.opsForList().remove(key, count, value);
			return TRUE;
		} catch (Exception e) {
			log.error("redis-lRemove-error:" + e.getMessage());
			return FALSE;
		}
	}

	//TODO LOCK


}