/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.dataexchange;

import java.util.Set;

/**
 * 批量数据交换结果。
 * 
 * @author datagear@163.com
 *
 */
public interface BatchDataExchangeResult
{
	/**
	 * 等待所有子数据交换执行完成。
	 * 
	 * @throws InterruptedException
	 */
	void waitForFinish() throws InterruptedException;

	/**
	 * 是否所有子数据交换已完成。
	 * 
	 * @return
	 */
	boolean isFinish();

	/**
	 * 获取当前还未提交的{@linkplain SubDataExchange}集合。
	 * 
	 * @return
	 */
	Set<SubDataExchange> getUnsubmits();

	/**
	 * 获取当前提交成功的{@linkplain SubDataExchange}数目。
	 * 
	 * @return
	 */
	int getSubmitSuccessCount();

	/**
	 * 获取当前提交成功的{@linkplain SubDataExchange}集合。
	 * 
	 * @return
	 */
	Set<SubDataExchange> getSubmitSuccesses();

	/**
	 * 获取当前提交失败的{@linkplain SubDataExchange}集合。
	 * 
	 * @return
	 */
	Set<SubDataExchange> getSubmitFails();

	/**
	 * 获取当前已取消的{@linkplain SubDataExchange}集合。
	 * 
	 * @return
	 */
	Set<SubDataExchange> getCancelleds();

	/**
	 * 获取当前执行完成的{@linkplain SubDataExchange}集合。
	 * 
	 * @return
	 */
	Set<SubDataExchange> getFinishes();

	/**
	 * 提交下一批具备执行条件的{@linkplain SubDataExchange}。
	 * <p>
	 * 具备执行条件：无任何依赖，或者，所有依赖都已执行完成。
	 * </p>
	 * <p>
	 * 返回空集合表示当前没有具备执行条件的{@linkplain SubDataExchange}。
	 * </p>
	 * 
	 * @return
	 */
	Set<SubDataExchange> submit();

	/**
	 * 取消指定{@linkplain SubDataExchange}。
	 * <p>
	 * 如果取消成功，将可在{@linkplain #getCancelleds()}中获取对应的{@linkplain SubDataExchange}。
	 * </p>
	 * 
	 * @param subDataExchangeId
	 */
	void cancel(String subDataExchangeId);
}
