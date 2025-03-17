package ir.maktabsharif.finalprojectphase12.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        // Default error message
        String errorMsg = "An unexpected error occurred";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMsg = "Page not found - " + statusCode;
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorMsg = "Access denied - " + statusCode;
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorMsg = "Internal server error - " + statusCode;
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                errorMsg = "Bad request - " + statusCode;
            }

            // Add the error message to the model
            model.addAttribute("errorMessage", errorMsg);
        } else {
            // Handle cases where the status code is not available
            model.addAttribute("errorMessage", errorMessage);
        }

        // Log the error
        System.err.println("Error occurred: " + errorMsg);

        return "error/error";
    }
    }


/*@Controller
@Slf4j
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        String errorMsg = "An unexpected error occurred";
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            errorMsg = switch (statusCode) {
                case 404 -> "Page not found - " + statusCode;
                case 403 -> "Access denied - " + statusCode;
                case 500 -> "Internal server error - " + statusCode;
                case 400 -> "Bad request - " + statusCode;
                default -> errorMsg;
            };
            model.addAttribute("errorMessage", errorMsg);
        } else {
            model.addAttribute("errorMessage", errorMessage);
        }

        log.error("Error occurred: {}", errorMsg);
        return "error/error";
    }
}*/





