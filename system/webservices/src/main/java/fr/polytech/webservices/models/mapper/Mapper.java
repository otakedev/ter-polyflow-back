package fr.polytech.webservices.models.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.polytech.webservices.models.mapper.errors.BadAccessGetterSetterMethod;
import fr.polytech.webservices.models.mapper.errors.BadFieldException;
import fr.polytech.webservices.models.mapper.errors.BadGetterSetterPrototype;
import fr.polytech.webservices.models.mapper.errors.SetterGetterNotFoundException;

public class Mapper<T1, T2> {

    public interface MapperID<T3> {
        public T3 getObjectById(Object id);
    }

    T1 body;

    public Mapper(T1 body) {
        this.body = body;
    }

    public T2 updateSimpleFields(T2 element) throws BadAccessGetterSetterMethod, BadGetterSetterPrototype,
            InvocationTargetException, BadFieldException, SetterGetterNotFoundException {
        Class<? extends Object> curClass = body.getClass();
        Method[] allMethods = curClass.getMethods();
        Map<String, Object> values = new HashMap<>();
        //Get all setter of the new body and get all values to set from body
        for (Method method : allMethods) {
            if (method.getName() == "getClass")
                continue;
            if (method.getName().startsWith("get")) {
                //Getter found, call it to get value and set as key the settername
                Object value;
                try {
                    value = method.invoke(body);
                } catch (IllegalAccessException e) {
                    throw new BadAccessGetterSetterMethod(
                            String.format("The method %s.%s.%s must be public to be used by the mapper",
                                    method.getDeclaringClass().getPackageName(), method.getDeclaringClass().getName(),
                                    method.getName()));
                } catch (IllegalArgumentException e) {
                    throw new BadGetterSetterPrototype(String.format("The method %s.%s.%s is not a getter valid",
                            method.getDeclaringClass().getPackageName(), method.getDeclaringClass().getName(),
                            method.getName()));
                }
                if (value != null)
                    values.put("set" + method.getName().substring(3), value);
            }
        }
        //Apply all setter with all value
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String fieldNamePascal = entry.getKey().substring(3);
            String fieldName = Character.toLowerCase(fieldNamePascal.charAt(0)) + fieldNamePascal.substring(1);
            Field fieldBody, fieldElement;
            //Get fields associated to check if it's the same type
            try {
                fieldBody = body.getClass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                throw new BadFieldException(String.format("Field %s.%s.%s not found",
                        this.body.getClass().getPackageName(), this.body.getClass().getName(), fieldName));
            } catch (SecurityException e) {
                throw new BadFieldException(String.format("The field %s.%s.%s must be public to be used by the mapper",
                        this.body.getClass().getPackageName(), this.body.getClass().getName(), fieldName));
            }
            try {
                fieldElement = element.getClass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                throw new BadFieldException(String.format("Field %s.%s.%s not found",
                        element.getClass().getPackageName(), element.getClass().getName(), fieldName));
            } catch (SecurityException e) {
                throw new BadFieldException(String.format("The field %s.%s.%s must be public to be used by the mapper",
                        element.getClass().getPackageName(), element.getClass().getName(), fieldName));
            }
            if (fieldBody.getType().equals(fieldElement.getType())) {
                //If the type is not equals or it's List of elements that are not same type continue
                if (fieldBody.getType().equals(List.class)
                        && !fieldBody.getGenericType().equals(fieldElement.getGenericType()))
                    continue;
                Method method;
                //Get the type to have the prototype of the method (required to find the method)
                Class<?> type;
                try {
                    type = body.getClass().getDeclaredField(fieldName).getType();
                } catch (NoSuchFieldException e) {
                    throw new BadFieldException(String.format("Field %s.%s.%s not found",
                            element.getClass().getPackageName(), element.getClass().getName(), fieldName));
                } catch (SecurityException e) {
                    throw new BadFieldException(
                            String.format("The field %s.%s.%s must be public to be used by the mapper",
                                    element.getClass().getPackageName(), element.getClass().getName(), fieldName));
                }
                //Get the setter Method
                try {
                    method = element.getClass().getDeclaredMethod(entry.getKey(), type);
                } catch (NoSuchMethodException e) {
                    throw new SetterGetterNotFoundException(String.format("Method %s.%s.%s not found",
                            element.getClass().getPackageName(), element.getClass().getName(), entry.getKey()));
                } catch (SecurityException e) {
                    throw new BadGetterSetterPrototype(
                            String.format("The method %s.%s.%s must be public to be used by the mapper",
                                    element.getClass().getPackageName(), element.getClass().getName(), entry.getKey()));
                }
                //Set the value of the model
                try {
                    method.invoke(element, entry.getValue());
                } catch (IllegalAccessException e) {
                    throw new BadAccessGetterSetterMethod(
                            String.format("The method %s.%s.%s must be public to be used by the mapper",
                                    method.getDeclaringClass().getPackageName(), method.getDeclaringClass().getName(),
                                    method.getName()));
                } catch (IllegalArgumentException e) {
                    throw new BadGetterSetterPrototype(String.format("The method %s.%s.%s is not a getter valid",
                            method.getDeclaringClass().getPackageName(), method.getDeclaringClass().getName(),
                            method.getName()));
                }
            }
        }
        return element;
    }

    public <T3> T2 changeElementById(T2 element, String fieldName, MapperID<T3> mapper)
            throws SetterGetterNotFoundException, BadFieldException, BadGetterSetterPrototype,
            InvocationTargetException, BadAccessGetterSetterMethod {
        String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        
        //Get type of the field to have the prototype of the method (required to find the method)
        Class<?> type;
        try {
            type = element.getClass().getDeclaredField(fieldName).getType();
        } catch (NoSuchFieldException e) {
            throw new SetterGetterNotFoundException(String.format("Method %s.%s.%s not found",
                    element.getClass().getPackageName(), element.getClass().getName(), fieldName));
        } catch (SecurityException e) {
            throw new BadFieldException(String.format("The field %s.%s.%s must be public to be used by the mapper",
                    element.getClass().getPackageName(), element.getClass().getName(), fieldName));
        }
        //Get the setter method
        Method setter;
        try {
            setter = element.getClass().getDeclaredMethod(setterName, type);
        } catch (NoSuchMethodException e) {
            throw new SetterGetterNotFoundException(String.format("Method %s.%s.%s not found",
                    element.getClass().getPackageName(), element.getClass().getName(), setterName));
        } catch (SecurityException e) {
            throw new BadGetterSetterPrototype(
                    String.format("The method %s.%s.%s must be public to be used by the mapper",
                            element.getClass().getPackageName(), element.getClass().getName(), setterName));
        }
        //Get the getter method
        Method getter;
        try {
            getter = body.getClass().getDeclaredMethod(getterName);
        } catch (NoSuchMethodException e) {
            throw new SetterGetterNotFoundException(String.format("Method %s.%s.%s not found",
                    body.getClass().getPackageName(), body.getClass().getName(), getterName));
        } catch (SecurityException e) {
            throw new BadGetterSetterPrototype(
                    String.format("The method %s.%s.%s must be public to be used by the mapper",
                            body.getClass().getPackageName(), body.getClass().getName(), getterName));
        }
        //Get the id with getter method
        Object id;
        try {
            id = getter.invoke(this.body);
        } catch (IllegalAccessException e) {
            throw new BadAccessGetterSetterMethod(
                    String.format("The method %s.%s.%s must be public to be used by the mapper",
                            getter.getDeclaringClass().getPackageName(), getter.getDeclaringClass().getName(),
                            getter.getName()));
        } catch (IllegalArgumentException e) {
            throw new BadGetterSetterPrototype(String.format("The method %s.%s.%s is not a getter valid",
                    getter.getDeclaringClass().getPackageName(), getter.getDeclaringClass().getName(),
                    getter.getName()));
        }
        //If id is null abort
        if (id == null)
            return element;
        //Replace value with mapper who convert id to object
        try {
            setter.invoke(element, mapper.getObjectById(id));
        } catch (IllegalAccessException e) {
            throw new BadAccessGetterSetterMethod(
                    String.format("The method %s.%s.%s must be public to be used by the mapper",
                            setter.getDeclaringClass().getPackageName(), setter.getDeclaringClass().getName(),
                            setter.getName()));
        } catch (IllegalArgumentException e) {
            throw new BadGetterSetterPrototype(String.format("The method %s.%s.%s is not a getter valid",
                    setter.getDeclaringClass().getPackageName(), setter.getDeclaringClass().getName(),
                    setter.getName()));
        }
        return element;
    }

    @SuppressWarnings("unchecked")
    public <T3> T2 changeElementsById(T2 element, String fieldName, MapperID<T3> mapper)
            throws InvocationTargetException, BadGetterSetterPrototype, SetterGetterNotFoundException,
            BadAccessGetterSetterMethod, BadFieldException {
        String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        //Get getter method from element to parse
        Method getterElement;
        try {
            getterElement = element.getClass().getDeclaredMethod(getterName);
        } catch (NoSuchMethodException e) {
            throw new SetterGetterNotFoundException(String.format("Method %s.%s.%s not found",
                    element.getClass().getPackageName(), element.getClass().getName(), getterName));
        } catch (SecurityException e) {
            throw new BadGetterSetterPrototype(
                    String.format("The method %s.%s.%s must be public to be used by the mapper",
                            element.getClass().getPackageName(), element.getClass().getName(), getterName));
        }
        //Get getter method from body
        Method getterOriginal;
        try {
            getterOriginal = this.body.getClass().getDeclaredMethod(getterName);
        } catch (NoSuchMethodException e) {
            throw new SetterGetterNotFoundException(String.format("Method %s.%s.%s not found",
                    body.getClass().getPackageName(), body.getClass().getName(), getterName));
        } catch (SecurityException e) {
            throw new BadGetterSetterPrototype(
                    String.format("The method %s.%s.%s must be public to be used by the mapper",
                            body.getClass().getPackageName(), body.getClass().getName(), getterName));
        }
        Class<?> typeReturnGetterElement = getterElement.getReturnType();
        Class<?> typeReturnGetterOriginal = getterOriginal.getReturnType();
        //Check if the attribute is a list
        if (List.class.isAssignableFrom(typeReturnGetterElement)
                && List.class.isAssignableFrom(typeReturnGetterOriginal)) {
            List<Object> ids;
            //Get list of id
            try {
                ids = (List<Object>) getterOriginal.invoke(this.body);
            } catch (IllegalAccessException e) {
                throw new BadAccessGetterSetterMethod(
                        String.format("The method %s.%s.%s must be public to be used by the mapper",
                                getterOriginal.getDeclaringClass().getPackageName(),
                                getterOriginal.getDeclaringClass().getName(), getterOriginal.getName()));
            } catch (IllegalArgumentException e) {
                throw new BadGetterSetterPrototype(String.format("The method %s.%s.%s is not a getter valid",
                        getterOriginal.getDeclaringClass().getPackageName(),
                        getterOriginal.getDeclaringClass().getName(), getterOriginal.getName()));
            }
            if (ids == null)
                return element;
            //Get the current list
            List<T3> objects;
            try {
                objects = (List<T3>) getterElement.invoke(element);
            } catch (IllegalAccessException e) {
                throw new BadAccessGetterSetterMethod(
                        String.format("The method %s.%s.%s must be public to be used by the mapper",
                            getterElement.getDeclaringClass().getPackageName(),
                            getterElement.getDeclaringClass().getName(), getterElement.getName()));
            } catch (IllegalArgumentException e) {
                throw new BadGetterSetterPrototype(String.format("The method %s.%s.%s is not a getter valid",
                    getterElement.getDeclaringClass().getPackageName(),
                    getterElement.getDeclaringClass().getName(), getterElement.getName()));
            }
            //Clear the current list and replace with the mapping object
            objects.clear();
            objects.addAll(ids.stream().map(id -> mapper.getObjectById(id)).collect(Collectors.toList()));
        }
        else {
            throw new BadFieldException(String.format("The field %s.%s.%s or %s.%s.%s must be a list to be used by the mapper",
                        element.getClass().getPackageName(), element.getClass().getName(), fieldName,
                        body.getClass().getPackageName(), body.getClass().getName(), fieldName));
        }
        return element;
    }

    public T2 render(T2 element) {
        try {
            return this.updateSimpleFields(element);
        } catch (Exception e) {
            return element;
        }
    }
}
