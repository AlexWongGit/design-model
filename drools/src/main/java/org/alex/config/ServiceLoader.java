//package org.alex.config;
//
//import jdk.internal.access.JavaLangAccess;
//import jdk.internal.access.SharedSecrets;
//import jdk.internal.loader.BootLoader;
//import jdk.internal.loader.ClassLoaders;
//import jdk.internal.misc.VM;
//import jdk.internal.module.ServicesCatalog;
//import jdk.internal.reflect.CallerSensitive;
//import jdk.internal.reflect.Reflection;
//import sun.nio.cs.UTF_8;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.lang.reflect.Modifier;
//import java.net.URL;
//import java.net.URLConnection;
//import java.security.*;
//import java.util.*;
//import java.util.function.Consumer;
//import java.util.function.Supplier;
//import java.util.stream.Stream;
//import java.util.stream.StreamSupport;
//
//public class ServiceLoader<S> implements Iterable<S>{
//    // The class or interface representing the service being loaded
//    private final Class<S> service;
//
//    // The class of the service type
//    private final String serviceName;
//
//    // The module layer used to locate providers; null when locating
//    // providers using a class loader
//    private final ModuleLayer layer;
//
//    // The class loader used to locate, load, and instantiate providers;
//    // null when locating provider using a module layer
//    private final ClassLoader loader;
//
//    // The access control context taken when the ServiceLoader is created
//    private final AccessControlContext acc;
//
//    // The lazy-lookup iterator for iterator operations
//    private Iterator<java.util.ServiceLoader.Provider<S>> lookupIterator1;
//    private final List<S> instantiatedProviders = new ArrayList<>();
//
//    // The lazy-lookup iterator for stream operations
//    private Iterator<java.util.ServiceLoader.Provider<S>> lookupIterator2;
//    private final List<java.util.ServiceLoader.Provider<S>> loadedProviders = new ArrayList<>();
//    private boolean loadedAllProviders; // true when all providers loaded
//
//    // Incremented when reload is called
//    private int reloadCount;
//
//    private static JavaLangAccess LANG_ACCESS;
//    static {
//        LANG_ACCESS = SharedSecrets.getJavaLangAccess();
//    }
//
//    /**
//     * Represents a service provider located by {@code ServiceLoader}.
//     *
//     * <p> When using a loader's {@link java.util.ServiceLoader#stream() stream()} method
//     * then the elements are of type {@code Provider}. This allows processing
//     * to select or filter on the provider class without instantiating the
//     * provider. </p>
//     *
//     * @param  <S> The service type
//     * @since 9
//     * @spec JPMS
//     */
//    public static interface Provider<S> extends Supplier<S> {
//        /**
//         * Returns the provider type. There is no guarantee that this type is
//         * accessible or that it has a public no-args constructor. The {@link
//         * #get() get()} method should be used to obtain the provider instance.
//         *
//         * <p> When a module declares that the provider class is created by a
//         * provider factory then this method returns the return type of its
//         * public static "{@code provider()}" method.
//         *
//         * @return The provider type
//         */
//        Class<? extends S> type();
//
//        /**
//         * Returns an instance of the provider.
//         *
//         * @return An instance of the provider.
//         *
//         * @throws ServiceConfigurationError
//         *         If the service provider cannot be instantiated, or in the
//         *         case of a provider factory, the public static
//         *         "{@code provider()}" method returns {@code null} or throws
//         *         an error or exception. The {@code ServiceConfigurationError}
//         *         will carry an appropriate cause where possible.
//         */
//        @Override S get();
//    }
//
//    /**
//     * Initializes a new instance of this class for locating service providers
//     * in a module layer.
//     *
//     * @throws ServiceConfigurationError
//     *         If {@code svc} is not accessible to {@code caller} or the caller
//     *         module does not use the service type.
//     */
//    private ServiceLoader(Class<?> caller, ModuleLayer layer, Class<S> svc) {
//        Objects.requireNonNull(caller);
//        Objects.requireNonNull(layer);
//        Objects.requireNonNull(svc);
//        checkCaller(caller, svc);
//
//        this.service = svc;
//        this.serviceName = svc.getName();
//        this.layer = layer;
//        this.loader = null;
//        this.acc = (System.getSecurityManager() != null)
//                ? AccessController.getContext()
//                : null;
//    }
//
//    /**
//     * Initializes a new instance of this class for locating service providers
//     * via a class loader.
//     *
//     * @throws ServiceConfigurationError
//     *         If {@code svc} is not accessible to {@code caller} or the caller
//     *         module does not use the service type.
//     */
//    private ServiceLoader(Class<?> caller, Class<S> svc, ClassLoader cl) {
//        Objects.requireNonNull(svc);
//
//        if (VM.isBooted()) {
//            checkCaller(caller, svc);
//            if (cl == null) {
//                cl = ClassLoader.getSystemClassLoader();
//            }
//        } else {
//
//            // if we get here then it means that ServiceLoader is being used
//            // before the VM initialization has completed. At this point then
//            // only code in the java.base should be executing.
//            Module callerModule = caller.getModule();
//            Module base = Object.class.getModule();
//            Module svcModule = svc.getModule();
//            if (callerModule != base || svcModule != base) {
//                fail(svc, "not accessible to " + callerModule + " during VM init");
//            }
//
//            // restricted to boot loader during startup
//            cl = null;
//        }
//
//        this.service = svc;
//        this.serviceName = svc.getName();
//        this.layer = null;
//        this.loader = cl;
//        this.acc = (System.getSecurityManager() != null)
//                ? AccessController.getContext()
//                : null;
//    }
//
//    /**
//     * Initializes a new instance of this class for locating service providers
//     * via a class loader.
//     *
//     * @apiNote For use by ResourceBundle
//     *
//     * @throws ServiceConfigurationError
//     *         If the caller module does not use the service type.
//     */
//    private ServiceLoader(Module callerModule, Class<S> svc, ClassLoader cl) {
//        if (!callerModule.canUse(svc)) {
//            fail(svc, callerModule + " does not declare `uses`");
//        }
//
//        this.service = Objects.requireNonNull(svc);
//        this.serviceName = svc.getName();
//        this.layer = null;
//        this.loader = cl;
//        this.acc = (System.getSecurityManager() != null)
//                ? AccessController.getContext()
//                : null;
//    }
//
//    /**
//     * Checks that the given service type is accessible to types in the given
//     * module, and check that the module declares that it uses the service type.
//     */
//    private static void checkCaller(Class<?> caller, Class<?> svc) {
//        if (caller == null) {
//            fail(svc, "no caller to check if it declares `uses`");
//        }
//
//        // Check access to the service type
//        Module callerModule = caller.getModule();
//        int mods = svc.getModifiers();
//        if (!Reflection.verifyMemberAccess(caller, svc, null, mods)) {
//            fail(svc, "service type not accessible to " + callerModule);
//        }
//
//        // If the caller is in a named module then it should "uses" the
//        // service type
//        if (!callerModule.canUse(svc)) {
//            fail(svc, callerModule + " does not declare `uses`");
//        }
//    }
//
//    private static void fail(Class<?> service, String msg, Throwable cause)
//            throws ServiceConfigurationError
//    {
//        throw new ServiceConfigurationError(service.getName() + ": " + msg,
//                cause);
//    }
//
//    private static void fail(Class<?> service, String msg)
//            throws ServiceConfigurationError
//    {
//        throw new ServiceConfigurationError(service.getName() + ": " + msg);
//    }
//
//    private static void fail(Class<?> service, URL u, int line, String msg)
//            throws ServiceConfigurationError
//    {
//        fail(service, u + ":" + line + ": " + msg);
//    }
//
//    /**
//     * Returns {@code true} if the provider is in an explicit module
//     */
//    private boolean inExplicitModule(Class<?> clazz) {
//        Module module = clazz.getModule();
//        return module.isNamed() && !module.getDescriptor().isAutomatic();
//    }
//
//    /**
//     * Returns the public static "provider" method if found.
//     *
//     * @throws ServiceConfigurationError if there is an error finding the
//     *         provider method or there is more than one public static
//     *         provider method
//     */
//    private Method findStaticProviderMethod(Class<?> clazz) {
//        List<Method> methods = null;
//        try {
//            methods = LANG_ACCESS.getDeclaredPublicMethods(clazz, "provider");
//        } catch (Throwable x) {
//            fail(service, "Unable to get public provider() method", x);
//        }
//        if (methods.isEmpty()) {
//            // does not declare a public provider method
//            return null;
//        }
//
//        // locate the static methods, can be at most one
//        Method result = null;
//        for (Method method : methods) {
//            int mods = method.getModifiers();
//            assert Modifier.isPublic(mods);
//            if (Modifier.isStatic(mods)) {
//                if (result != null) {
//                    fail(service, clazz + " declares more than one"
//                            + " public static provider() method");
//                }
//                result = method;
//            }
//        }
//        if (result != null) {
//            Method m = result;
//            PrivilegedAction<Void> pa = () -> {
//                m.setAccessible(true);
//                return null;
//            };
//            AccessController.doPrivileged(pa);
//        }
//        return result;
//    }
//
//    /**
//     * Returns the public no-arg constructor of a class.
//     *
//     * @throws ServiceConfigurationError if the class does not have
//     *         public no-arg constructor
//     */
//    private Constructor<?> getConstructor(Class<?> clazz) {
//        PrivilegedExceptionAction<Constructor<?>> pa
//                = new PrivilegedExceptionAction<>() {
//            @Override
//            public Constructor<?> run() throws Exception {
//                Constructor<?> ctor = clazz.getConstructor();
//                if (inExplicitModule(clazz))
//                    ctor.setAccessible(true);
//                return ctor;
//            }
//        };
//        Constructor<?> ctor = null;
//        try {
//            ctor = AccessController.doPrivileged(pa);
//        } catch (Throwable x) {
//            if (x instanceof PrivilegedActionException)
//                x = x.getCause();
//            String cn = clazz.getName();
//            fail(service, cn + " Unable to get public no-arg constructor", x);
//        }
//        return ctor;
//    }
//
//    /**
//     * A Provider implementation that supports invoking, with reduced
//     * permissions, the static factory to obtain the provider or the
//     * provider's no-arg constructor.
//     */
//    private static class ProviderImpl<S> implements java.util.ServiceLoader.Provider<S> {
//        final Class<S> service;
//        final Class<? extends S> type;
//        final Method factoryMethod;  // factory method or null
//        final Constructor<? extends S> ctor; // public no-args constructor or null
//        final AccessControlContext acc;
//
//        ProviderImpl(Class<S> service,
//                     Class<? extends S> type,
//                     Method factoryMethod,
//                     AccessControlContext acc) {
//            this.service = service;
//            this.type = type;
//            this.factoryMethod = factoryMethod;
//            this.ctor = null;
//            this.acc = acc;
//        }
//
//        ProviderImpl(Class<S> service,
//                     Class<? extends S> type,
//                     Constructor<? extends S> ctor,
//                     AccessControlContext acc) {
//            this.service = service;
//            this.type = type;
//            this.factoryMethod = null;
//            this.ctor = ctor;
//            this.acc = acc;
//        }
//
//        @Override
//        public Class<? extends S> type() {
//            return type;
//        }
//
//        @Override
//        public S get() {
//            if (factoryMethod != null) {
//                return invokeFactoryMethod();
//            } else {
//                return newInstance();
//            }
//        }
//
//        /**
//         * Invokes the provider's "provider" method to instantiate a provider.
//         * When running with a security manager then the method runs with
//         * permissions that are restricted by the security context of whatever
//         * created this loader.
//         */
//        private S invokeFactoryMethod() {
//            Object result = null;
//            Throwable exc = null;
//            if (acc == null) {
//                try {
//                    result = factoryMethod.invoke(null);
//                } catch (Throwable x) {
//                    exc = x;
//                }
//            } else {
//                PrivilegedExceptionAction<?> pa = new PrivilegedExceptionAction<>() {
//                    @Override
//                    public Object run() throws Exception {
//                        return factoryMethod.invoke(null);
//                    }
//                };
//                // invoke factory method with permissions restricted by acc
//                try {
//                    result = AccessController.doPrivileged(pa, acc);
//                } catch (Throwable x) {
//                    if (x instanceof PrivilegedActionException)
//                        x = x.getCause();
//                    exc = x;
//                }
//            }
//            if (exc != null) {
//                if (exc instanceof InvocationTargetException)
//                    exc = exc.getCause();
//                fail(service, factoryMethod + " failed", exc);
//            }
//            if (result == null) {
//                fail(service, factoryMethod + " returned null");
//            }
//            @SuppressWarnings("unchecked")
//            S p = (S) result;
//            return p;
//        }
//
//        /**
//         * Invokes Constructor::newInstance to instantiate a provider. When running
//         * with a security manager then the constructor runs with permissions that
//         * are restricted by the security context of whatever created this loader.
//         */
//        private S newInstance() {
//            S p = null;
//            Throwable exc = null;
//            if (acc == null) {
//                try {
//                    p = ctor.newInstance();
//                } catch (Throwable x) {
//                    exc = x;
//                }
//            } else {
//                PrivilegedExceptionAction<S> pa = new PrivilegedExceptionAction<>() {
//                    @Override
//                    public S run() throws Exception {
//                        return ctor.newInstance();
//                    }
//                };
//                // invoke constructor with permissions restricted by acc
//                try {
//                    p = AccessController.doPrivileged(pa, acc);
//                } catch (Throwable x) {
//                    if (x instanceof PrivilegedActionException)
//                        x = x.getCause();
//                    exc = x;
//                }
//            }
//            if (exc != null) {
//                if (exc instanceof InvocationTargetException)
//                    exc = exc.getCause();
//                String cn = ctor.getDeclaringClass().getName();
//                fail(service,
//                        "Provider " + cn + " could not be instantiated", exc);
//            }
//            return p;
//        }
//
//        // For now, equals/hashCode uses the access control context to ensure
//        // that two Providers created with different contexts are not equal
//        // when running with a security manager.
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(service, type, acc);
//        }
//
//        @Override
//        public boolean equals(Object ob) {
//            if (!(ob instanceof java.util.ServiceLoader.ProviderImpl))
//                return false;
//            @SuppressWarnings("unchecked")
//            java.util.ServiceLoader.ProviderImpl<?> that = (java.util.ServiceLoader.ProviderImpl<?>)ob;
//            return this.service == that.service
//                    && this.type == that.type
//                    && Objects.equals(this.acc, that.acc);
//        }
//    }
//
//    /**
//     * Loads a service provider in a module.
//     *
//     * Returns {@code null} if the service provider's module doesn't read
//     * the module with the service type.
//     *
//     * @throws ServiceConfigurationError if the class cannot be loaded or
//     *         isn't the expected sub-type (or doesn't define a provider
//     *         factory method that returns the expected type)
//     */
//    private java.util.ServiceLoader.Provider<S> loadProvider(ServicesCatalog.ServiceProvider provider) {
//        Module module = provider.module();
//        if (!module.canRead(service.getModule())) {
//            // module does not read the module with the service type
//            return null;
//        }
//
//        String cn = provider.providerName();
//        Class<?> clazz = null;
//        if (acc == null) {
//            try {
//                clazz = Class.forName(module, cn);
//            } catch (LinkageError e) {
//                fail(service, "Unable to load " + cn, e);
//            }
//        } else {
//            PrivilegedExceptionAction<Class<?>> pa = () -> Class.forName(module, cn);
//            try {
//                clazz = AccessController.doPrivileged(pa);
//            } catch (Throwable x) {
//                if (x instanceof PrivilegedActionException)
//                    x = x.getCause();
//                fail(service, "Unable to load " + cn, x);
//                return null;
//            }
//        }
//        if (clazz == null) {
//            fail(service, "Provider " + cn + " not found");
//        }
//
//        int mods = clazz.getModifiers();
//        if (!Modifier.isPublic(mods)) {
//            fail(service, clazz + " is not public");
//        }
//
//        // if provider in explicit module then check for static factory method
//        if (inExplicitModule(clazz)) {
//            Method factoryMethod = findStaticProviderMethod(clazz);
//            if (factoryMethod != null) {
//                Class<?> returnType = factoryMethod.getReturnType();
//                if (!service.isAssignableFrom(returnType)) {
//                    fail(service, factoryMethod + " return type not a subtype");
//                }
//
//                @SuppressWarnings("unchecked")
//                Class<? extends S> type = (Class<? extends S>) returnType;
//                return new java.util.ServiceLoader.ProviderImpl<S>(service, type, factoryMethod, acc);
//            }
//        }
//
//        // no factory method so must be a subtype
//        if (!service.isAssignableFrom(clazz)) {
//            fail(service, clazz.getName() + " not a subtype");
//        }
//
//        @SuppressWarnings("unchecked")
//        Class<? extends S> type = (Class<? extends S>) clazz;
//        @SuppressWarnings("unchecked")
//        Constructor<? extends S> ctor = (Constructor<? extends S> ) getConstructor(clazz);
//        return new java.util.ServiceLoader.ProviderImpl<S>(service, type, ctor, acc);
//    }
//
//    /**
//     * Implements lazy service provider lookup of service providers that
//     * are provided by modules in a module layer (or parent layers)
//     */
//    private final class LayerLookupIterator<T>
//            implements Iterator<java.util.ServiceLoader.Provider<T>>
//    {
//        Deque<ModuleLayer> stack = new ArrayDeque<>();
//        Set<ModuleLayer> visited = new HashSet<>();
//        Iterator<ServicesCatalog.ServiceProvider> iterator;
//
//        java.util.ServiceLoader.Provider<T> nextProvider;
//        ServiceConfigurationError nextError;
//
//        LayerLookupIterator() {
//            visited.add(layer);
//            stack.push(layer);
//        }
//
//        private Iterator<ServicesCatalog.ServiceProvider> providers(ModuleLayer layer) {
//            ServicesCatalog catalog = LANG_ACCESS.getServicesCatalog(layer);
//            return catalog.findServices(serviceName).iterator();
//        }
//
//        @Override
//        public boolean hasNext() {
//            while (nextProvider == null && nextError == null) {
//                // get next provider to load
//                while (iterator == null || !iterator.hasNext()) {
//                    // next layer (DFS order)
//                    if (stack.isEmpty())
//                        return false;
//
//                    ModuleLayer layer = stack.pop();
//                    List<ModuleLayer> parents = layer.parents();
//                    for (int i = parents.size() - 1; i >= 0; i--) {
//                        ModuleLayer parent = parents.get(i);
//                        if (!visited.contains(parent)) {
//                            visited.add(parent);
//                            stack.push(parent);
//                        }
//                    }
//                    iterator = providers(layer);
//                }
//
//                // attempt to load provider
//                ServicesCatalog.ServiceProvider provider = iterator.next();
//                try {
//                    @SuppressWarnings("unchecked")
//                    java.util.ServiceLoader.Provider<T> next = (java.util.ServiceLoader.Provider<T>) loadProvider(provider);
//                    nextProvider = next;
//                } catch (ServiceConfigurationError e) {
//                    nextError = e;
//                }
//            }
//            return true;
//        }
//
//        @Override
//        public java.util.ServiceLoader.Provider<T> next() {
//            if (!hasNext())
//                throw new NoSuchElementException();
//
//            java.util.ServiceLoader.Provider<T> provider = nextProvider;
//            if (provider != null) {
//                nextProvider = null;
//                return provider;
//            } else {
//                ServiceConfigurationError e = nextError;
//                assert e != null;
//                nextError = null;
//                throw e;
//            }
//        }
//    }
//
//    /**
//     * Implements lazy service provider lookup of service providers that
//     * are provided by modules defined to a class loader or to modules in
//     * layers with a module defined to the class loader.
//     */
//    private final class ModuleServicesLookupIterator<T>
//            implements Iterator<java.util.ServiceLoader.Provider<T>>
//    {
//        ClassLoader currentLoader;
//        Iterator<ServicesCatalog.ServiceProvider> iterator;
//
//        java.util.ServiceLoader.Provider<T> nextProvider;
//        ServiceConfigurationError nextError;
//
//        ModuleServicesLookupIterator() {
//            this.currentLoader = loader;
//            this.iterator = iteratorFor(loader);
//        }
//
//        /**
//         * Returns iterator to iterate over the implementations of {@code
//         * service} in the given layer.
//         */
//        private List<ServicesCatalog.ServiceProvider> providers(ModuleLayer layer) {
//            ServicesCatalog catalog = LANG_ACCESS.getServicesCatalog(layer);
//            return catalog.findServices(serviceName);
//        }
//
//        /**
//         * Returns the class loader that a module is defined to
//         */
//        private ClassLoader loaderFor(Module module) {
//            SecurityManager sm = System.getSecurityManager();
//            if (sm == null) {
//                return module.getClassLoader();
//            } else {
//                PrivilegedAction<ClassLoader> pa = module::getClassLoader;
//                return AccessController.doPrivileged(pa);
//            }
//        }
//
//        /**
//         * Returns an iterator to iterate over the implementations of {@code
//         * service} in modules defined to the given class loader or in custom
//         * layers with a module defined to this class loader.
//         */
//        private Iterator<ServicesCatalog.ServiceProvider> iteratorFor(ClassLoader loader) {
//            // modules defined to the class loader
//            ServicesCatalog catalog;
//            if (loader == null) {
//                catalog = BootLoader.getServicesCatalog();
//            } else {
//                catalog = ServicesCatalog.getServicesCatalogOrNull(loader);
//            }
//            List<ServicesCatalog.ServiceProvider> providers;
//            if (catalog == null) {
//                providers = List.of();
//            } else {
//                providers = catalog.findServices(serviceName);
//            }
//
//            // modules in layers that define modules to the class loader
//            ClassLoader platformClassLoader = ClassLoaders.platformClassLoader();
//            if (loader == null || loader == platformClassLoader) {
//                return providers.iterator();
//            } else {
//                List<ServicesCatalog.ServiceProvider> allProviders = new ArrayList<>(providers);
//                Iterator<ModuleLayer> iterator = LANG_ACCESS.layers(loader).iterator();
//                while (iterator.hasNext()) {
//                    ModuleLayer layer = iterator.next();
//                    for (ServicesCatalog.ServiceProvider sp : providers(layer)) {
//                        ClassLoader l = loaderFor(sp.module());
//                        if (l != null && l != platformClassLoader) {
//                            allProviders.add(sp);
//                        }
//                    }
//                }
//                return allProviders.iterator();
//            }
//        }
//
//        @Override
//        public boolean hasNext() {
//            while (nextProvider == null && nextError == null) {
//                // get next provider to load
//                while (!iterator.hasNext()) {
//                    if (currentLoader == null) {
//                        return false;
//                    } else {
//                        currentLoader = currentLoader.getParent();
//                        iterator = iteratorFor(currentLoader);
//                    }
//                }
//
//                // attempt to load provider
//                ServicesCatalog.ServiceProvider provider = iterator.next();
//                try {
//                    @SuppressWarnings("unchecked")
//                    java.util.ServiceLoader.Provider<T> next = (java.util.ServiceLoader.Provider<T>) loadProvider(provider);
//                    nextProvider = next;
//                } catch (ServiceConfigurationError e) {
//                    nextError = e;
//                }
//            }
//            return true;
//        }
//
//        @Override
//        public java.util.ServiceLoader.Provider<T> next() {
//            if (!hasNext())
//                throw new NoSuchElementException();
//
//            java.util.ServiceLoader.Provider<T> provider = nextProvider;
//            if (provider != null) {
//                nextProvider = null;
//                return provider;
//            } else {
//                ServiceConfigurationError e = nextError;
//                assert e != null;
//                nextError = null;
//                throw e;
//            }
//        }
//    }
//
//    /**
//     * Implements lazy service provider lookup where the service providers are
//     * configured via service configuration files. Service providers in named
//     * modules are silently ignored by this lookup iterator.
//     */
//    private final class LazyClassPathLookupIterator<T>
//            implements Iterator<java.util.ServiceLoader.Provider<T>>
//    {
//        //扫描目录前缀
//        static final String PREFIX = "META-INF/services/";
//
//        Set<String> providerNames = new HashSet<>();  // to avoid duplicates
//        Enumeration<URL> configs;
//        Iterator<String> pending;
//
//        java.util.ServiceLoader.Provider<T> nextProvider;
//        ServiceConfigurationError nextError;
//
//        LazyClassPathLookupIterator() { }
//
//        /**
//         * Parse a single line from the given configuration file, adding the
//         * name on the line to set of names if not already seen.
//         */
//        private int parseLine(URL u, BufferedReader r, int lc, Set<String> names)
//                throws IOException
//        {
//            String ln = r.readLine();
//            if (ln == null) {
//                return -1;
//            }
//            int ci = ln.indexOf('#');
//            if (ci >= 0) ln = ln.substring(0, ci);
//            ln = ln.trim();
//            int n = ln.length();
//            if (n != 0) {
//                if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0))
//                    fail(service, u, lc, "Illegal configuration-file syntax");
//                int cp = ln.codePointAt(0);
//                if (!Character.isJavaIdentifierStart(cp))
//                    fail(service, u, lc, "Illegal provider-class name: " + ln);
//                int start = Character.charCount(cp);
//                for (int i = start; i < n; i += Character.charCount(cp)) {
//                    cp = ln.codePointAt(i);
//                    if (!Character.isJavaIdentifierPart(cp) && (cp != '.'))
//                        fail(service, u, lc, "Illegal provider-class name: " + ln);
//                }
//                if (providerNames.add(ln)) {
//                    names.add(ln);
//                }
//            }
//            return lc + 1;
//        }
//
//        /**
//         * Parse the content of the given URL as a provider-configuration file.
//         */
//        private Iterator<String> parse(URL u) {
//            Set<String> names = new LinkedHashSet<>(); // preserve insertion order
//            try {
//                URLConnection uc = u.openConnection();
//                uc.setUseCaches(false);
//                try (InputStream in = uc.getInputStream();
//                     BufferedReader r
//                             = new BufferedReader(new InputStreamReader(in, UTF_8.INSTANCE)))
//                {
//                    int lc = 1;
//                    while ((lc = parseLine(u, r, lc, names)) >= 0);
//                }
//            } catch (IOException x) {
//                fail(service, "Error accessing configuration file", x);
//            }
//            return names.iterator();
//        }
//
//        /**
//         * Loads and returns the next provider class.
//         */
//        private Class<?> nextProviderClass() {
//            if (configs == null) {
//                try {
//                    String fullName = PREFIX + service.getName();
//                    if (loader == null) {
//                        configs = ClassLoader.getSystemResources(fullName);
//                    } else if (loader == ClassLoaders.platformClassLoader()) {
//                        // The platform classloader doesn't have a class path,
//                        // but the boot loader might.
//                        if (BootLoader.hasClassPath()) {
//                            configs = BootLoader.findResources(fullName);
//                        } else {
//                            configs = Collections.emptyEnumeration();
//                        }
//                    } else {
//                        configs = loader.getResources(fullName);
//                    }
//                } catch (IOException x) {
//                    fail(service, "Error locating configuration files", x);
//                }
//            }
//            while ((pending == null) || !pending.hasNext()) {
//                if (!configs.hasMoreElements()) {
//                    return null;
//                }
//                pending = parse(configs.nextElement());
//            }
//            String cn = pending.next();
//            try {
//                return Class.forName(cn, false, loader);
//            } catch (ClassNotFoundException x) {
//                fail(service, "Provider " + cn + " not found");
//                return null;
//            }
//        }
//
//        @SuppressWarnings("unchecked")
//        private boolean hasNextService() {
//            while (nextProvider == null && nextError == null) {
//                try {
//                    Class<?> clazz = nextProviderClass();
//                    if (clazz == null)
//                        return false;
//
//                    if (clazz.getModule().isNamed()) {
//                        // ignore class if in named module
//                        continue;
//                    }
//
//                    if (service.isAssignableFrom(clazz)) {
//                        Class<? extends S> type = (Class<? extends S>) clazz;
//                        Constructor<? extends S> ctor
//                                = (Constructor<? extends S>)getConstructor(clazz);
//                        java.util.ServiceLoader.ProviderImpl<S> p = new java.util.ServiceLoader.ProviderImpl<S>(service, type, ctor, acc);
//                        nextProvider = (java.util.ServiceLoader.ProviderImpl<T>) p;
//                    } else {
//                        fail(service, clazz.getName() + " not a subtype");
//                    }
//                } catch (ServiceConfigurationError e) {
//                    nextError = e;
//                }
//            }
//            return true;
//        }
//
//        private java.util.ServiceLoader.Provider<T> nextService() {
//            if (!hasNextService())
//                throw new NoSuchElementException();
//
//            java.util.ServiceLoader.Provider<T> provider = nextProvider;
//            if (provider != null) {
//                nextProvider = null;
//                return provider;
//            } else {
//                ServiceConfigurationError e = nextError;
//                assert e != null;
//                nextError = null;
//                throw e;
//            }
//        }
//
//        @Override
//        public boolean hasNext() {
//            if (acc == null) {
//                return hasNextService();
//            } else {
//                PrivilegedAction<Boolean> action = new PrivilegedAction<>() {
//                    public Boolean run() { return hasNextService(); }
//                };
//                return AccessController.doPrivileged(action, acc);
//            }
//        }
//
//        @Override
//        public java.util.ServiceLoader.Provider<T> next() {
//            if (acc == null) {
//                return nextService();
//            } else {
//                PrivilegedAction<java.util.ServiceLoader.Provider<T>> action = new PrivilegedAction<>() {
//                    public java.util.ServiceLoader.Provider<T> run() { return nextService(); }
//                };
//                return AccessController.doPrivileged(action, acc);
//            }
//        }
//    }
//
//    /**
//     * Returns a new lookup iterator.
//     */
//    private Iterator<java.util.ServiceLoader.Provider<S>> newLookupIterator() {
//        assert layer == null || loader == null;
//        if (layer != null) {
//            return new java.util.ServiceLoader.LayerLookupIterator<>();
//        } else {
//            Iterator<java.util.ServiceLoader.Provider<S>> first = new java.util.ServiceLoader.ModuleServicesLookupIterator<>();
//            Iterator<java.util.ServiceLoader.Provider<S>> second = new java.util.ServiceLoader.LazyClassPathLookupIterator<>();
//            return new Iterator<java.util.ServiceLoader.Provider<S>>() {
//                @Override
//                public boolean hasNext() {
//                    return (first.hasNext() || second.hasNext());
//                }
//                @Override
//                public java.util.ServiceLoader.Provider<S> next() {
//                    if (first.hasNext()) {
//                        return first.next();
//                    } else if (second.hasNext()) {
//                        return second.next();
//                    } else {
//                        throw new NoSuchElementException();
//                    }
//                }
//            };
//        }
//    }
//
//    /**
//     * Returns an iterator to lazily load and instantiate the available
//     * providers of this loader's service.
//     *
//     * <p> To achieve laziness the actual work of locating and instantiating
//     * providers is done by the iterator itself. Its {@link Iterator#hasNext
//     * hasNext} and {@link Iterator#next next} methods can therefore throw a
//     * {@link ServiceConfigurationError} for any of the reasons specified in
//     * the <a href="#errors">Errors</a> section above. To write robust code it
//     * is only necessary to catch {@code ServiceConfigurationError} when using
//     * the iterator. If an error is thrown then subsequent invocations of the
//     * iterator will make a best effort to locate and instantiate the next
//     * available provider, but in general such recovery cannot be guaranteed.
//     *
//     * <p> Caching: The iterator returned by this method first yields all of
//     * the elements of the provider cache, in the order that they were loaded.
//     * It then lazily loads and instantiates any remaining service providers,
//     * adding each one to the cache in turn. If this loader's provider caches are
//     * cleared by invoking the {@link #reload() reload} method then existing
//     * iterators for this service loader should be discarded.
//     * The {@code  hasNext} and {@code next} methods of the iterator throw {@link
//     * java.util.ConcurrentModificationException ConcurrentModificationException}
//     * if used after the provider cache has been cleared.
//     *
//     * <p> The iterator returned by this method does not support removal.
//     * Invoking its {@link java.util.Iterator#remove() remove} method will
//     * cause an {@link UnsupportedOperationException} to be thrown.
//     *
//     * @apiNote Throwing an error in these cases may seem extreme.  The rationale
//     * for this behavior is that a malformed provider-configuration file, like a
//     * malformed class file, indicates a serious problem with the way the Java
//     * virtual machine is configured or is being used.  As such it is preferable
//     * to throw an error rather than try to recover or, even worse, fail silently.
//     *
//     * @return  An iterator that lazily loads providers for this loader's
//     *          service
//     *
//     * @revised 9
//     * @spec JPMS
//     */
//    public Iterator<S> iterator() {
//
//        // create lookup iterator if needed
//        if (lookupIterator1 == null) {
//            lookupIterator1 = newLookupIterator();
//        }
//
//        return new Iterator<S>() {
//
//            // record reload count
//            final int expectedReloadCount = java.util.ServiceLoader.this.reloadCount;
//
//            // index into the cached providers list
//            int index;
//
//            /**
//             * Throws ConcurrentModificationException if the list of cached
//             * providers has been cleared by reload.
//             */
//            private void checkReloadCount() {
//                if (java.util.ServiceLoader.this.reloadCount != expectedReloadCount)
//                    throw new ConcurrentModificationException();
//            }
//
//            @Override
//            public boolean hasNext() {
//                checkReloadCount();
//                if (index < instantiatedProviders.size())
//                    return true;
//                return lookupIterator1.hasNext();
//            }
//
//            @Override
//            public S next() {
//                checkReloadCount();
//                S next;
//                if (index < instantiatedProviders.size()) {
//                    next = instantiatedProviders.get(index);
//                } else {
//                    next = lookupIterator1.next().get();
//                    instantiatedProviders.add(next);
//                }
//                index++;
//                return next;
//            }
//
//        };
//    }
//
//    /**
//     * Returns a stream to lazily load available providers of this loader's
//     * service. The stream elements are of type {@link java.util.ServiceLoader.Provider Provider}, the
//     * {@code Provider}'s {@link java.util.ServiceLoader.Provider#get() get} method must be invoked to
//     * get or instantiate the provider.
//     *
//     * <p> To achieve laziness the actual work of locating providers is done
//     * when processing the stream. If a service provider cannot be loaded for any
//     * of the reasons specified in the <a href="#errors">Errors</a> section
//     * above then {@link ServiceConfigurationError} is thrown by whatever method
//     * caused the service provider to be loaded. </p>
//     *
//     * <p> Caching: When processing the stream then providers that were previously
//     * loaded by stream operations are processed first, in load order. It then
//     * lazily loads any remaining service providers. If this loader's provider
//     * caches are cleared by invoking the {@link #reload() reload} method then
//     * existing streams for this service loader should be discarded. The returned
//     * stream's source {@link Spliterator spliterator} is <em>fail-fast</em> and
//     * will throw {@link ConcurrentModificationException} if the provider cache
//     * has been cleared. </p>
//     *
//     * <p> The following examples demonstrate usage. The first example creates
//     * a stream of {@code CodecFactory} objects, the second example is the same
//     * except that it sorts the providers by provider class name (and so locate
//     * all providers).
//     * <pre>{@code
//     *    Stream<CodecFactory> providers = ServiceLoader.load(CodecFactory.class)
//     *            .stream()
//     *            .map(Provider::get);
//     *
//     *    Stream<CodecFactory> providers = ServiceLoader.load(CodecFactory.class)
//     *            .stream()
//     *            .sorted(Comparator.comparing(p -> p.type().getName()))
//     *            .map(Provider::get);
//     * }</pre>
//     *
//     * @return  A stream that lazily loads providers for this loader's service
//     *
//     * @since 9
//     * @spec JPMS
//     */
//    public Stream<java.util.ServiceLoader.Provider<S>> stream() {
//        // use cached providers as the source when all providers loaded
//        if (loadedAllProviders) {
//            return loadedProviders.stream();
//        }
//
//        // create lookup iterator if needed
//        if (lookupIterator2 == null) {
//            lookupIterator2 = newLookupIterator();
//        }
//
//        // use lookup iterator and cached providers as source
//        Spliterator<java.util.ServiceLoader.Provider<S>> s = new java.util.ServiceLoader.ProviderSpliterator<>(lookupIterator2);
//        return StreamSupport.stream(s, false);
//    }
//
//    private class ProviderSpliterator<T> implements Spliterator<java.util.ServiceLoader.Provider<T>> {
//        final int expectedReloadCount = java.util.ServiceLoader.this.reloadCount;
//        final Iterator<java.util.ServiceLoader.Provider<T>> iterator;
//        int index;
//
//        ProviderSpliterator(Iterator<java.util.ServiceLoader.Provider<T>> iterator) {
//            this.iterator = iterator;
//        }
//
//        @Override
//        public Spliterator<java.util.ServiceLoader.Provider<T>> trySplit() {
//            return null;
//        }
//
//        @Override
//        @SuppressWarnings("unchecked")
//        public boolean tryAdvance(Consumer<? super java.util.ServiceLoader.Provider<T>> action) {
//            if (java.util.ServiceLoader.this.reloadCount != expectedReloadCount)
//                throw new ConcurrentModificationException();
//            java.util.ServiceLoader.Provider<T> next = null;
//            if (index < loadedProviders.size()) {
//                next = (java.util.ServiceLoader.Provider<T>) loadedProviders.get(index++);
//            } else if (iterator.hasNext()) {
//                next = iterator.next();
//                loadedProviders.add((java.util.ServiceLoader.Provider<S>)next);
//                index++;
//            } else {
//                loadedAllProviders = true;
//            }
//            if (next != null) {
//                action.accept(next);
//                return true;
//            } else {
//                return false;
//            }
//        }
//
//        @Override
//        public int characteristics() {
//            // not IMMUTABLE as structural interference possible
//            // not NOTNULL so that the characteristics are a subset of the
//            // characteristics when all Providers have been located.
//            return Spliterator.ORDERED;
//        }
//
//        @Override
//        public long estimateSize() {
//            return Long.MAX_VALUE;
//        }
//    }
//
//    /**
//     * Creates a new service loader for the given service type, class
//     * loader, and caller.
//     *
//     * @param  <S> the class of the service type
//     *
//     * @param  service
//     *         The interface or abstract class representing the service
//     *
//     * @param  loader
//     *         The class loader to be used to load provider-configuration files
//     *         and provider classes, or {@code null} if the system class
//     *         loader (or, failing that, the bootstrap class loader) is to be
//     *         used
//     *
//     * @param  callerModule
//     *         The caller's module for which a new service loader is created
//     *
//     * @return A new service loader
//     */
//    static <S> java.util.ServiceLoader<S> load(Class<S> service,
//                                               ClassLoader loader,
//                                               Module callerModule)
//    {
//        return new java.util.ServiceLoader<>(callerModule, service, loader);
//    }
//
//    /**
//     * Creates a new service loader for the given service. The service loader
//     * uses the given class loader as the starting point to locate service
//     * providers for the service. The service loader's {@link #iterator()
//     * iterator} and {@link #stream() stream} locate providers in both named
//     * and unnamed modules, as follows:
//     *
//     * <ul>
//     *   <li> <p> Step 1: Locate providers in named modules. </p>
//     *
//     *   <p> Service providers are located in all named modules of the class
//     *   loader or to any class loader reachable via parent delegation. </p>
//     *
//     *   <p> In addition, if the class loader is not the bootstrap or {@linkplain
//     *   ClassLoader#getPlatformClassLoader() platform class loader}, then service
//     *   providers may be located in the named modules of other class loaders.
//     *   Specifically, if the class loader, or any class loader reachable via
//     *   parent delegation, has a module in a {@linkplain ModuleLayer module
//     *   layer}, then service providers in all modules in the module layer are
//     *   located.  </p>
//     *
//     *   <p> For example, suppose there is a module layer where each module is
//     *   in its own class loader (see {@link ModuleLayer#defineModulesWithManyLoaders
//     *   defineModulesWithManyLoaders}). If this {@code ServiceLoader.load} method
//     *   is invoked to locate providers using any of the class loaders created for
//     *   the module layer, then it will locate all of the providers in the module
//     *   layer, irrespective of their defining class loader. </p>
//     *
//     *   <p> Ordering: The service loader will first locate any service providers
//     *   in modules defined to the class loader, then its parent class loader,
//     *   its parent parent, and so on to the bootstrap class loader. If a class
//     *   loader has modules in a module layer then all providers in that module
//     *   layer are located (irrespective of their class loader) before the
//     *   providers in the parent class loader are located. The ordering of
//     *   modules in same class loader, or the ordering of modules in a module
//     *   layer, is not defined. </p>
//     *
//     *   <p> If a module declares more than one provider then the providers
//     *   are located in the order that its module descriptor {@linkplain
//     *   java.lang.module.ModuleDescriptor.Provides#providers() lists the
//     *   providers}. Providers added dynamically by instrumentation agents (see
//     *   {@link java.lang.instrument.Instrumentation#redefineModule redefineModule})
//     *   are always located after providers declared by the module. </p> </li>
//     *
//     *   <li> <p> Step 2: Locate providers in unnamed modules. </p>
//     *
//     *   <p> Service providers in unnamed modules are located if their class names
//     *   are listed in provider-configuration files located by the class loader's
//     *   {@link ClassLoader#getResources(String) getResources} method. </p>
//     *
//     *   <p> The ordering is based on the order that the class loader's {@code
//     *   getResources} method finds the service configuration files and within
//     *   that, the order that the class names are listed in the file. </p>
//     *
//     *   <p> In a provider-configuration file, any mention of a service provider
//     *   that is deployed in a named module is ignored. This is to avoid
//     *   duplicates that would otherwise arise when a named module has both a
//     *   <i>provides</i> directive and a provider-configuration file that mention
//     *   the same service provider. </p>
//     *
//     *   <p> The provider class must be visible to the class loader. </p> </li>
//     *
//     * </ul>
//     *
//     * @apiNote If the class path of the class loader includes remote network
//     * URLs then those URLs may be dereferenced in the process of searching for
//     * provider-configuration files.
//     *
//     * <p> This activity is normal, although it may cause puzzling entries to be
//     * created in web-server logs.  If a web server is not configured correctly,
//     * however, then this activity may cause the provider-loading algorithm to fail
//     * spuriously.
//     *
//     * <p> A web server should return an HTTP 404 (Not Found) response when a
//     * requested resource does not exist.  Sometimes, however, web servers are
//     * erroneously configured to return an HTTP 200 (OK) response along with a
//     * helpful HTML error page in such cases.  This will cause a {@link
//     * ServiceConfigurationError} to be thrown when this class attempts to parse
//     * the HTML page as a provider-configuration file.  The best solution to this
//     * problem is to fix the misconfigured web server to return the correct
//     * response code (HTTP 404) along with the HTML error page.
//     *
//     * @param  <S> the class of the service type
//     *
//     * @param  service
//     *         The interface or abstract class representing the service
//     *
//     * @param  loader
//     *         The class loader to be used to load provider-configuration files
//     *         and provider classes, or {@code null} if the system class
//     *         loader (or, failing that, the bootstrap class loader) is to be
//     *         used
//     *
//     * @return A new service loader
//     *
//     * @throws ServiceConfigurationError
//     *         if the service type is not accessible to the caller or the
//     *         caller is in an explicit module and its module descriptor does
//     *         not declare that it uses {@code service}
//     *
//     * @revised 9
//     * @spec JPMS
//     */
//    @CallerSensitive
//    public static <S> java.util.ServiceLoader<S> load(Class<S> service,
//                                                      ClassLoader loader)
//    {
//        return new java.util.ServiceLoader<>(Reflection.getCallerClass(), service, loader);
//    }
//
//    /**
//     * Creates a new service loader for the given service type, using the
//     * current thread's {@linkplain java.lang.Thread#getContextClassLoader
//     * context class loader}.
//     *
//     * <p> An invocation of this convenience method of the form
//     * <pre>{@code
//     *     ServiceLoader.load(service)
//     * }</pre>
//     *
//     * is equivalent to
//     *
//     * <pre>{@code
//     *     ServiceLoader.load(service, Thread.currentThread().getContextClassLoader())
//     * }</pre>
//     *
//     * @apiNote Service loader objects obtained with this method should not be
//     * cached VM-wide. For example, different applications in the same VM may
//     * have different thread context class loaders. A lookup by one application
//     * may locate a service provider that is only visible via its thread
//     * context class loader and so is not suitable to be located by the other
//     * application. Memory leaks can also arise. A thread local may be suited
//     * to some applications.
//     *
//     * @param  <S> the class of the service type
//     *
//     * @param  service
//     *         The interface or abstract class representing the service
//     *
//     * @return A new service loader
//     *
//     * @throws ServiceConfigurationError
//     *         if the service type is not accessible to the caller or the
//     *         caller is in an explicit module and its module descriptor does
//     *         not declare that it uses {@code service}
//     *
//     * @revised 9
//     * @spec JPMS
//     */
//    @CallerSensitive
//    public static <S> java.util.ServiceLoader<S> load(Class<S> service) {
//        ClassLoader cl = Thread.currentThread().getContextClassLoader();
//        return new java.util.ServiceLoader<>(Reflection.getCallerClass(), service, cl);
//    }
//
//    /**
//     * Creates a new service loader for the given service type, using the
//     * {@linkplain ClassLoader#getPlatformClassLoader() platform class loader}.
//     *
//     * <p> This convenience method is equivalent to: </p>
//     *
//     * <pre>{@code
//     *     ServiceLoader.load(service, ClassLoader.getPlatformClassLoader())
//     * }</pre>
//     *
//     * <p> This method is intended for use when only installed providers are
//     * desired.  The resulting service will only find and load providers that
//     * have been installed into the current Java virtual machine; providers on
//     * the application's module path or class path will be ignored.
//     *
//     * @param  <S> the class of the service type
//     *
//     * @param  service
//     *         The interface or abstract class representing the service
//     *
//     * @return A new service loader
//     *
//     * @throws ServiceConfigurationError
//     *         if the service type is not accessible to the caller or the
//     *         caller is in an explicit module and its module descriptor does
//     *         not declare that it uses {@code service}
//     *
//     * @revised 9
//     * @spec JPMS
//     */
//    @CallerSensitive
//    public static <S> java.util.ServiceLoader<S> loadInstalled(Class<S> service) {
//        ClassLoader cl = ClassLoader.getPlatformClassLoader();
//        return new java.util.ServiceLoader<>(Reflection.getCallerClass(), service, cl);
//    }
//
//    /**
//     * Creates a new service loader for the given service type to load service
//     * providers from modules in the given module layer and its ancestors. It
//     * does not locate providers in unnamed modules. The ordering that the service
//     * loader's {@link #iterator() iterator} and {@link #stream() stream} locate
//     * providers and yield elements is as follows:
//     *
//     * <ul>
//     *   <li><p> Providers are located in a module layer before locating providers
//     *   in parent layers. Traversal of parent layers is depth-first with each
//     *   layer visited at most once. For example, suppose L0 is the boot layer, L1
//     *   and L2 are modules layers with L0 as their parent. Now suppose that L3 is
//     *   created with L1 and L2 as the parents (in that order). Using a service
//     *   loader to locate providers with L3 as the context will locate providers
//     *   in the following order: L3, L1, L0, L2. </p></li>
//     *
//     *   <li><p> If a module declares more than one provider then the providers
//     *   are located in the order that its module descriptor
//     *   {@linkplain java.lang.module.ModuleDescriptor.Provides#providers()
//     *   lists the providers}. Providers added dynamically by instrumentation
//     *   agents are always located after providers declared by the module. </p></li>
//     *
//     *   <li><p> The ordering of modules in a module layer is not defined. </p></li>
//     * </ul>
//     *
//     * @apiNote Unlike the other load methods defined here, the service type
//     * is the second parameter. The reason for this is to avoid source
//     * compatibility issues for code that uses {@code load(S, null)}.
//     *
//     * @param  <S> the class of the service type
//     *
//     * @param  layer
//     *         The module layer
//     *
//     * @param  service
//     *         The interface or abstract class representing the service
//     *
//     * @return A new service loader
//     *
//     * @throws ServiceConfigurationError
//     *         if the service type is not accessible to the caller or the
//     *         caller is in an explicit module and its module descriptor does
//     *         not declare that it uses {@code service}
//     *
//     * @since 9
//     * @spec JPMS
//     */
//    @CallerSensitive
//    public static <S> java.util.ServiceLoader<S> load(ModuleLayer layer, Class<S> service) {
//        return new java.util.ServiceLoader<>(Reflection.getCallerClass(), layer, service);
//    }
//
//    /**
//     * Load the first available service provider of this loader's service. This
//     * convenience method is equivalent to invoking the {@link #iterator()
//     * iterator()} method and obtaining the first element. It therefore
//     * returns the first element from the provider cache if possible, it
//     * otherwise attempts to load and instantiate the first provider.
//     *
//     * <p> The following example loads the first available service provider. If
//     * no service providers are located then it uses a default implementation.
//     * <pre>{@code
//     *    CodecFactory factory = ServiceLoader.load(CodecFactory.class)
//     *                                        .findFirst()
//     *                                        .orElse(DEFAULT_CODECSET_FACTORY);
//     * }</pre>
//     * @return The first service provider or empty {@code Optional} if no
//     *         service providers are located
//     *
//     * @throws ServiceConfigurationError
//     *         If a provider class cannot be loaded for any of the reasons
//     *         specified in the <a href="#errors">Errors</a> section above.
//     *
//     * @since 9
//     * @spec JPMS
//     */
//    public Optional<S> findFirst() {
//        Iterator<S> iterator = iterator();
//        if (iterator.hasNext()) {
//            return Optional.of(iterator.next());
//        } else {
//            return Optional.empty();
//        }
//    }
//
//    /**
//     * Clear this loader's provider cache so that all providers will be
//     * reloaded.
//     *
//     * <p> After invoking this method, subsequent invocations of the {@link
//     * #iterator() iterator} or {@link #stream() stream} methods will lazily
//     * locate providers (and instantiate in the case of {@code iterator})
//     * from scratch, just as is done by a newly-created service loader.
//     *
//     * <p> This method is intended for use in situations in which new service
//     * providers can be installed into a running Java virtual machine.
//     */
//    public void reload() {
//        lookupIterator1 = null;
//        instantiatedProviders.clear();
//
//        lookupIterator2 = null;
//        loadedProviders.clear();
//        loadedAllProviders = false;
//
//        // increment count to allow CME be thrown
//        reloadCount++;
//    }
//
//    /**
//     * Returns a string describing this service.
//     *
//     * @return  A descriptive string
//     */
//    public String toString() {
//        return "java.util.ServiceLoader[" + service.getName() + "]";
//    }
//
//}
