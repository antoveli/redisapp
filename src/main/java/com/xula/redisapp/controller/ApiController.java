package com.xula.redisapp.controller;

import com.xula.redisapp.datos.Respuesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/apidredis")
public class ApiController
{

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @GetMapping("/registrar")
    public ResponseEntity<?> almacenar(@RequestBody Respuesta respuesta)
    {
       if( almacenarDatos(respuesta))
       {
           return ResponseEntity.ok().build();
       }

       return ResponseEntity.badRequest().build();
    }

    @GetMapping("/consulta")
    public ResponseEntity<?> consulta(@RequestBody Respuesta consulta)
    {
        if(redisTemplate.hasKey(consulta.getFormulario()))
        {
            System.out.println( "Consulta de la clave " + consulta.getFormulario() );
            String respuesta  =  redisTemplate.opsForValue().get(consulta.getFormulario());
            System.out.println("Se econtro el valor: " + respuesta);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    public boolean almacenarDatos(Respuesta respuesta)
    {
        String clave =  respuesta.getFormulario();
        try
        {
            boolean exito = redisTemplate.opsForValue().setIfAbsent(clave, "hola mundo");

            if(exito)
            {
                redisTemplate.expire(clave, 60, TimeUnit.SECONDS);
                return  exito;
            }
        }
        catch (Exception exception)
        {
            System.out.println( exception.toString());
        }
        return false;
    }


    @GetMapping("/consultartodos")
    public List<String> consultarTodos()
    {


        ScanOptions options = ScanOptions.scanOptions()
                .match("*") // Coincidir con todas las claves
                .count(100) // Cantidad de claves a recuperar en cada iteración
                .build();

        List<String> valores = new ArrayList<>();

        try (Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(options)) {
            while (cursor.hasNext()) {
                byte[] clave = cursor.next();
                String valor = redisTemplate.opsForValue().get(new String(clave, "UTF-8"));
                valores.add(valor);
            }
        } catch (Exception e) {
            // Manejar la excepción en caso de error
            e.printStackTrace();
        }

        System.out.println(valores);
        return valores;
    }

}
