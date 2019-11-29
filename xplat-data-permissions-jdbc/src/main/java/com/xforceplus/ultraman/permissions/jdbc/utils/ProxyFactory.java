package com.xforceplus.ultraman.permissions.jdbc.utils;

import com.xforceplus.ultraman.permissions.pojo.result.Result;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;

/**
 * 代理工厂,提供接口和实类两种方式来构造一个代理对象.
 * @version 1.0
 * @author Mike
 * @since 1.5
 */
public class ProxyFactory {

    /**
     * 提供以接口的方式构造一个被代理过的对象.
     * @param target 目标对象.
     * @param handle 拦截对象.
     * @return 已经被代理过的对象.
     */
    public static Object createInterfacetProxyFromObject(Object target, InvocationHandler handle) {
        return Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            handle);
    }

    /**
     * 直接构造一个接口的代理，使用当前线程的ClassLoader。
     * @param interfaceType 接口的Class。
     * @param handle 拦截对象的实现。
     * @return 代理对象。
     */
    public static Object createInterfactProxy(Class interfaceType, InvocationHandler handle) {
        return ProxyFactory.createInterfactProxy(
            interfaceType.getClassLoader(),
            interfaceType,
            handle);
    }

    /**
     * 使用给定的ClassLoader构造一个代理。
     * @param classLoader 类装载器。
     * @param interfaceType 接口类型。
     * @param handle 拦截对象的实现。
     * @return 代理对象。
     */
    public static Object createInterfactProxy(ClassLoader classLoader, Class interfaceType, InvocationHandler handle) {
        return Proxy.newProxyInstance(
            classLoader,
            new Class[]{interfaceType},
            handle);
    }

    //------------------------------cglib-----------------------------------------

    /**
     * 以子类的方式来实现代理,需要cglib.
     * 使用装备target对象的ClassLoader。
     * @param target 目标对象.
     * @param handle 拦截对象.
     * @return 目标对象的子类.
     */
    public static Object createObjectProxyFromObject(Object target, MethodInterceptor handle) {
        return ProxyFactory.createObjectProxy(
            target.getClass().getClassLoader(),
            target.getClass(),
            handle);
    }

    /**
     * 使用默认ClassLoader来构造代理。
     * @param interfaceType 需要代理的类型。
     * @param handle 拦截实现。
     * @return 代理对象。
     */
    public static Object createObjectProxy(Class interfaceType, MethodInterceptor handle) {
        return ProxyFactory.createObjectProxy(null, interfaceType, handle);
    }

    /**
     * 使用cglib来构造一个代理。
     * @param classLoader 如果为null将使用默认。
     * @param interfaceType 需要代理的类型。
     * @param handle 拦截实现。
     * @return 代理对象。
     */
    public static Object createObjectProxy(
        ClassLoader classLoader,
        Class interfaceType,
        MethodInterceptor handle) {

        if (interfaceType == null || handle == null) {
            throw new IllegalArgumentException("Intercepted and the interceptor instance of interface types can not be empty.");
        }

        Enhancer enhancer = new Enhancer();

        if (classLoader != null) {
            enhancer.setClassLoader(classLoader);
        }

        enhancer.setSuperclass(interfaceType);
        enhancer.setCallback(handle);
        return enhancer.create();
    }
}

