package ir.maktabsharif.finalprojectphase12.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public String handleBadCredentialsException(BadCredentialsException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("error", e.getMessage());
        return "redirect:/user/login";
    }
}


