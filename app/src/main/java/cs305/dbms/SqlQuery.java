package cs305.dbms;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlQuery<T> {
    private String rawQuery;
    public final String defaultParam = "value";

    public SqlQuery(String rawQuery) {
        this.rawQuery = rawQuery;
    }

    private <R> String getSqlLiteral(R param) throws PrimitiveNotImplementedException {
        Class<?> cls = param.getClass();
        if (cls == Integer.class) {
            Integer value = Integer.class.cast(param);
            return value.toString();
        } else if (cls == Float.class) {
            Float value = Float.class.cast(param);
            return value.toString();
        } else if (cls == Double.class) {
            Double value = Double.class.cast(param);
            return value.toString();
        } else if (cls == Boolean.class) {
            Boolean value = Boolean.class.cast(param);
            return value == true ? "TRUE" : "FALSE";
        } else if (cls == String.class) {
            String value = String.class.cast(param);
            // TODO: Manage SQL Injection
            return "\"" + value + "\"";
        } else if (cls == ArrayList.class) {
            ArrayList<?> value = ArrayList.class.cast(param);
            if (value.size() == 0)
                return "()";
            String string = "(" + getSqlLiteral(value.get(0));
            for (int i = 1; i < value.size(); i++) {
                string = string + "," + getSqlLiteral(value.get(i));
            }
            string = string + ")";
            return string;
        }
        throw new PrimitiveNotImplementedException(cls.getName());
    }

    private boolean isObject(T param) {
        Class<?> cls = param.getClass();
        Class<?> nonObjectClass[] = { Integer.class, Float.class, Double.class, Boolean.class, String.class,
                ArrayList.class, Short.class, Byte.class, Long.class, Date.class };
        for (int i = 0; i < nonObjectClass.length; i++) {
            if (cls == nonObjectClass[i]) {
                return false;
            }
        }
        return true;
    }

    private String getQuerySubstitutedWithSqlLiteral(String query, String paramName, String literal) {
        return query.replaceAll("\\$\\{" + paramName + "\\}", literal);
    }

    private String getQuerySubstitutedWithObject(T param)
            throws IllFormedParamException, IllegalArgumentException, PrimitiveNotImplementedException {
        Pattern pattern = Pattern.compile("\\$\\{([\\w_][\\w\\d_]*)\\}");
        Matcher matcher = pattern.matcher(this.rawQuery);
        String query = this.rawQuery;
        while (matcher.find()) {
            String paramName = matcher.group(1);
            try {
                Field field = param.getClass().getDeclaredField(paramName);
                String value = getSqlLiteral(field.get(param));
                query = this.getQuerySubstitutedWithSqlLiteral(query, paramName, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllFormedParamException(paramName, param.getClass().getName());
            }
        }
        return query;
    }

    private String getQuerySubstitutedWithNonObject(T param) throws PrimitiveNotImplementedException {
        return getQuerySubstitutedWithSqlLiteral(this.rawQuery, this.defaultParam, getSqlLiteral(param));
    }

    public String getQuery(T param)
            throws IllFormedParamException, IllegalArgumentException, PrimitiveNotImplementedException {
        if (isObject(param)) {
            return getQuerySubstitutedWithObject(param);
        } else {
            return getQuerySubstitutedWithNonObject(param);
        }
    }
}
