//package org.qin.common.interceptor;
//
//import org.apache.tomcat.util.http.parser.Authorization;
//import org.qin.exception.RemoteLoginException;
//import org.qin.exception.VersionNoException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.annotation.Reference;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.lang.reflect.Method;
//
///**
// * 自定义拦截器，判断此次请求是否有权限
// *
// * @author leijianhui
// */
//@Component
//public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
//
//    @Autowired
//    private StringRedisTemplate redis;
//
//    @Reference
//    private AdminUserService adminUserService;
//
//    private Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);
//
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //如果不是映射到方法直接通过
//        if (!(handler instanceof HandlerMethod)) {
//            return true;
//        }
//        String version = request.getHeader("version");
//        String platform = request.getHeader("platform");
//        if (Constants.PlatformType.android.getType().equals(platform)) {
//            Boolean isMember = redis.opsForSet().isMember(Constants.KEY_VERSION_AVAILABLE_ANDROID, version);
//            if (!isMember) {
//                VersionNoException versionNoException = new VersionNoException(redis.opsForValue().get(Constants.KEY_VERSION_LATEST_ANDROID));
//                throw versionNoException;
//            }
//        } else if (Constants.PlatformType.ios.getType().equals(platform)) {
//            Boolean isMember = redis.opsForSet().isMember(Constants.KEY_VERSION_AVAILABLE_IOS, version);
//            if (!isMember) {
//                VersionNoException versionNoException = new VersionNoException(redis.opsForValue().get(Constants.KEY_VERSION_LATEST_IOS));
//                throw versionNoException;
//            }
//        } else {
//            VersionNoException versionNoException = new VersionNoException("版本信息错误");
//            throw versionNoException;
//        }
//
//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        Method method = handlerMethod.getMethod();
//        //从header中得到token
//        String authorization = request.getHeader(Constants.AUTHORIZATION);
//        if (method.getAnnotation(Authorization.class) != null && !StringUtils.isEmpty(authorization)) {
//            Boolean hasKey = redis.hasKey(authorization);
//            String userId = null;
//            try {
//                if (authorization.equals("zyl")) {
//                    // 新增拦截测试token
//                    LoginSysModel sysUser = new LoginSysModel();
//                    sysUser.setAccount("wxy");
//                    sysUser.setPassword("123456");
//                    if (StringUtils.isEmpty(sysUser) || StringUtils.isEmpty(sysUser.getAccount())) {
//                        ResultModel.error(ResultStatus.PARAM_ERROR);
//                    }
//                    //  logger.info(sysUser.getAccount(),sysUser.getPassword());
//                    // 相当于重新登录一次.
//                    ResultModel<PcTokenModel> data = adminUserService.saveLoginMsg(sysUser);
//                    authorization = data.getData().getToken();
//                    userId = JwtUtil.checkTokenRtnUserId(authorization);
//                } else {
//                    userId = JwtUtil.checkTokenRtnUserId(authorization);
//                }
//            } catch (SignatureException e) {
//                if (!hasKey) {
//                    logger.error("token 失效:SignatureException");
//                    throw new SignatureException("token 失效", e);
//                }
//            } catch (ExpiredJwtException e) {
//                if (!hasKey) {
//                    logger.error("ExpiredJwtException", e);
//                    throw new SignatureException("token 失效", e);
//                }
//            } catch (Exception e) {
//                logger.error("Exception", e);
//                throw e;
//            }
//            if (!hasKey) {
//                /**
//                 * 抛出账号异地登入异常
//                 */
//                RemoteLoginException remoteLoginException = new RemoteLoginException();
//                throw remoteLoginException;
//            }
//            //如果token验证成功，将token对应的用户id存在request中，便于之后注入
//            request.setAttribute(Constants.CURRENT_USER_ID, userId);
//            return true;
//        }
//        //如果token为空，并且方法注明了Authorization，返回1000
//        if (method.getAnnotation(Authorization.class) != null && StringUtils.isEmpty(authorization)) {
//            response.setStatus(Constants.TOKEN_NULL);
//            return false;
//        }
//        //如果验证token失败，并且方法注明了Authorization，返回401错误
//        if (method.getAnnotation(Authorization.class) != null) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return false;
//        }
//        return true;
//    }
//}
