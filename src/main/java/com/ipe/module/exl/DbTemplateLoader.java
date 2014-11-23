package com.ipe.module.exl;

import java.io.IOException;
import java.io.Reader;

import org.springframework.stereotype.Repository;

import freemarker.cache.TemplateLoader;

@Repository
public class DbTemplateLoader implements TemplateLoader {
	
	
	@Override
	public void closeTemplateSource(Object arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object findTemplateSource(String arg0) throws IOException {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public long getLastModified(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Reader getReader(Object arg0, String arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
