package com.nong.nongo2o.entity.request;


import com.nong.nongo2o.base.Request;

import java.util.Date;

/**
 *
 *
 * @author wisdom
 * @date: 2017-07-18 21:20:27
 * @since V1.0
 */
public class CreateFollowRequest extends Request {
	/**
	* 被关注方用户编码
	*/
	private String targetCode;

	public CreateFollowRequest(String targetCode) {
		this.targetCode = targetCode;
	}
}