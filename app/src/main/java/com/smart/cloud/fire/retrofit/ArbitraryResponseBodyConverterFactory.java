package com.smart.cloud.fire.retrofit;

/**
 * Created by Administrator on 2016/9/8.
 */

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * Created by Administrator on 2016/9/8.
 */
public class ArbitraryResponseBodyConverterFactory<T> extends Converter.Factory {
    public static ArbitraryResponseBodyConverterFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static ArbitraryResponseBodyConverterFactory create(Gson gson) {
        return new ArbitraryResponseBodyConverterFactory(gson);
    }
    private final Gson gson;

    private ArbitraryResponseBodyConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new ArbitraryResponseBodyConverter<>(adapter);
    }

    class ArbitraryResponseBodyConverter<T> implements Converter<ResponseBody,  T>{
        private final TypeAdapter<T> adapter;
        ArbitraryResponseBodyConverter(TypeAdapter<T> adapter) {
            this.adapter = adapter;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            try {
                String str = new String(value.bytes(),"GB2312");
                return adapter.fromJson(str);
            } finally {
                value.close();
            }
        }
    }
}

