/**
 * 系统应用
 *
 * @author yubaoshan
 * @date 2020/5/26 19:06
 */
import { axios } from '@/utils/request'


/**
 * 登录
 *
 * @author yubaoshan
 * @date 2020/5/26 19:06
 */
export function login (parameter) {
  return axios({
    url: '/login',
    method: 'post',
    data: parameter
  })
}

/**
 * 登出
 *
 * @author yubaoshan
 * @date 2020/5/26 19:07
 */
export function logout (parameter) {
  return axios({
    url: '/logout',
    method: 'get',
    params: parameter
  })
}

/**
 * 获取登录用户信息
 *
 * @author yubaoshan
 * @date 2020/5/26 19:08
 */
export function getLoginUser (parameter) {
  return axios({
    url: '/getLoginUser',
    method: 'get',
    params: parameter
  })
}

/**
 * 获取用户头像
 *
 * @author yubaoshan
 * @date 2020/5/26 19:08
 */
export function previewAvatar (parameter) {
  return axios({
    url: '/system/previewAvatar',
    method: 'get',
    params: parameter
  })
}

/**
 * 获取短信验证码
 *
 * @author yubaoshan
 * @date 2020/5/26 19:29
 */
export function getSmsCaptcha (parameter) {
  return axios({
    url: '/getSmsCaptcha',
    method: 'get',
    params: parameter
  })
}

/**
 *
 *
 * @author yubaoshan
 * @date 2020/5/26 19:29
 */
export function twoStepCode (parameter) {
  return axios({
    url: '/auth/2step-code',
    method: 'get',
    params: parameter
  })
}

