package pt.isel.daw.business.logic;

import org.springframework.context.support.ResourceBundleMessageSource;
import pt.isel.daw.business.interfaces.ConstantService;

import java.util.Locale;

public class ConstantServiceImpl extends ResourceBundleMessageSource implements ConstantService {

    public String getConstant(String code, Object[] args) {
        return getMessage(code, args, Locale.getDefault());
    }

    public String getConstant(String code) {
        return getConstant(code, null);
    }
}