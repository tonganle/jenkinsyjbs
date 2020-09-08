/*
Copyright [2020] [https://www.stylefeng.cn]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Guns源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns-separation
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns-separation
6.若您的项目无法满足以上几点，可申请商业授权，获取Guns商业授权许可，请在官网购买授权，地址为 https://www.stylefeng.cn
 */
package cn.stylefeng.guns.sys.core.aop;

import cn.hutool.log.Log;
import cn.stylefeng.guns.core.annotion.BusinessLog;
import cn.stylefeng.guns.core.annotion.Permission;
import cn.stylefeng.guns.core.consts.AopSortConstant;
import cn.stylefeng.guns.core.context.login.LoginContextHolder;
import cn.stylefeng.guns.core.enums.LogicTypeEnum;
import cn.stylefeng.guns.core.exception.PermissionException;
import cn.stylefeng.guns.core.exception.enums.PermissionExceptionEnum;
import cn.stylefeng.guns.core.util.HttpServletUtil;
import cn.stylefeng.guns.sys.core.log.LogManager;
import org.apache.tomcat.util.buf.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 权限过滤Aop切面
 *
 * @author xuyuxiang
 * @date 2020/3/23 17:09
 */
@Aspect
@Order(AopSortConstant.PERMISSION_AOP)
public class PermissionAop {

    private static final Log log = Log.get();

    /**
     * 权限切入点
     *
     * @author xuyuxiang
     * @date 2020/3/23 17:10
     */
    @Pointcut("@annotation(cn.stylefeng.guns.core.annotion.Permission)")
    private void getPermissionPointCut() {
    }

    /**
     * 执行权限过滤
     *
     * @author xuyuxiang
     * @date 2020/3/23 17:14
     */
    @Before("getPermissionPointCut()")
    public void doPermission(JoinPoint joinPoint) {

        //如果当前登录用户是超级管理员则不校验权限
        boolean isSuperAdmin = LoginContextHolder.me().isSuperAdmin();
        if (!isSuperAdmin) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            Permission permission = method.getAnnotation(Permission.class);

            //当前方法需要的角色集合
            String[] requireRoles = permission.value();

            //逻辑类型
            LogicTypeEnum logicTypeEnum = permission.logicType();

            //如果不需要特别的角色，则判断用户所属角色是否有当前访问的url的权限
            if (requireRoles.length == 0) {
                HttpServletRequest request = HttpServletUtil.getRequest();
                boolean flag = LoginContextHolder.me().hasPermission(request.getRequestURI());
                if (!flag) {
                    this.executeNoPermissionExceptionLog(joinPoint, new PermissionException(PermissionExceptionEnum.NO_PERMISSION));
                    throw new PermissionException(PermissionExceptionEnum.NO_PERMISSION);
                }
            } else {
                //当前方法的权限需要一些特别的角色
                boolean flag = true;
                if (LogicTypeEnum.AND.equals(logicTypeEnum)) {
                    flag = LoginContextHolder.me().hasAllRole(StringUtils.join(requireRoles));
                } else if (LogicTypeEnum.OR.equals(logicTypeEnum)) {
                    flag = LoginContextHolder.me().hasAnyRole(StringUtils.join(requireRoles));
                } else {
                    log.error(">>> permission注解逻辑枚举错误");
                }
                if (!flag) {
                    this.executeNoPermissionExceptionLog(joinPoint, new PermissionException(PermissionExceptionEnum.NO_PERMISSION));
                    throw new PermissionException(PermissionExceptionEnum.NO_PERMISSION);
                }
            }
        }
    }

    /**
     * 记录无权限异常日志
     *
     * @author xuyuxiang
     * @date 2020/3/24 11:14
     */
    private void executeNoPermissionExceptionLog(JoinPoint joinPoint, Exception exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        BusinessLog businessLog = method.getAnnotation(BusinessLog.class);

        //异步记录日志
        LogManager.me().executeExceptionLog(
                businessLog, LoginContextHolder.me().getSysLoginUserAccount(), joinPoint, exception);
    }

}
