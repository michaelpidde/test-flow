package com.michaelpidde.automated.Frontend;

import com.michaelpidde.automated.Frontend.PageHandler;

import java.awt.Desktop;
import java.net.URI;
import org.eclipse.jetty.server.Server;

public class Frontend {
	public Frontend() throws Exception {
		Desktop.getDesktop().browse(new URI("http://localhost:9000"));
		Server server = new Server(9000);
		server.setHandler(new PageHandler());
		server.start();
		server.join();
	}
}