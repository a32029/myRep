package pt.isel.daw.business.interfaces;

import org.springframework.context.MessageSource;

public interface ConstantService extends MessageSource {
     String getConstant(String code, Object[] args);
     String getConstant(String code);
}