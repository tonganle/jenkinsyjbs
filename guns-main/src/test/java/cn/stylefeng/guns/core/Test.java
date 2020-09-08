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
package cn.stylefeng.guns.core;

import cn.stylefeng.guns.modular.model.car;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 测试类
 *
 * @author xuyuxiang
 * @date 2020/3/16 11:25
 */
public class Test extends BaseJunit {

    @org.junit.Test
    public void test() {
        car car1 = new car();
        car car2 = new car();
        car car3 = new car();
        car1.setId("1");
        car1.setName("a");
        car2.setId("2");
        car2.setName("b");
        car3.setId("3");
        car3.setName("c");
        ArrayList<car> list = new ArrayList<car>();
        list.add(car1);
        list.add(car2);
        list.add(car3);
        ArrayList<String> objects = new ArrayList<>();
        List<car> collect = list.stream().filter(s -> s.getId() == "1").filter(s -> s.getName() == "a").collect(Collectors.toList());
        collect.forEach(o -> objects.add(o.getName()));
        List<car> collect2 = list.stream().filter(s -> s.getId() == "1").filter(s -> s.getName() == "a").collect(Collectors.toList());
        collect2.forEach(o -> objects.add(o.getName()));
        System.out.println(objects);
        List<String> collect1 = objects.stream().distinct().collect(Collectors.toList());
        System.out.println(collect1);
    }
}
