package com.voice.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class VoiceRestApp 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(VoiceRestApp.class, args);
        System.out.println( "Hello World!" );
    }
}
