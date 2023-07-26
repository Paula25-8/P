package com.itinerariosdeaprendizaje.practicum;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
@Slf4j
@Controller
public class PracticumErrorController implements ErrorController {

    @Autowired
    ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest servletRequest) {
        Object status = servletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        ServletWebRequest servletWebRequest = new ServletWebRequest(servletRequest);

        // Quiero obtener la información de la excepcion y el mensaje
        Collection collection = new ArrayList();
        collection.add(ErrorAttributeOptions.Include.EXCEPTION);
        collection.add(ErrorAttributeOptions.Include.MESSAGE);
        Map<String, Object> errorMap = errorAttributes.getErrorAttributes(servletWebRequest, ErrorAttributeOptions.of(collection));
        log.error("ERROR: {}", errorMap);

        // Podemos crear una única página de error y pasar el mensaje correspondiente según el error
        // model.addAttribute("url", errorMap.get("path"));
        // model.addAttribute("msg", errorMap.get("error"));
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error/403";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/500";
            }
        }
        return "error";
    }

}
