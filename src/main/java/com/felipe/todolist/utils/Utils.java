package com.felipe.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

public class Utils {
    
    //função que retorna uma lista com os nomes dos atributos vazios de um objeto
    public static String[] getNullProprietyNames(Object source){
        final BeanWrapper src = new BeanWrapperImpl(source);//BeanWrapper é uma interface que permite manipular atributos e propriedades de um objeto
        PropertyDescriptor[] pds = src.getPropertyDescriptors();//criando um array com todas as propriedades do objeto

        Set<String> emptyNames = new HashSet<>();

        for(PropertyDescriptor pd:pds){
            Object srcValue = src.getPropertyValue(pd.getName());//pegando o valor de cada propriedade do objeto

            if(srcValue == null){
                emptyNames.add(pd.getName());
            }

        }

        String[] result = new String[emptyNames.size()];//Cria uma vetor result[] de mesmo tamanho que o vetor emptyNames[]
        return emptyNames.toArray(result);//converte o type de vetor Set para vetor String e retorna
    }

    //função que copia as os atributos não nulos de um objeto para outro
    public static void copyNonNullProprieties(Object source, Object target){
        BeanUtils.copyProperties(source, target, getNullProprietyNames(source));//esa função copia os atributos de source para target 
                                                                                //que nao estao presentes em getNullProprietyNames(source)
    }
}
