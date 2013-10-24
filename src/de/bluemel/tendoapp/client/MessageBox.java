package de.bluemel.tendoapp.client;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MessageBox extends DialogBox {

	public MessageBox(final String title, final String htmlMessageLine) {
		this(title, Arrays.asList(new String[] { htmlMessageLine }));
	}

	public MessageBox(final String title, final String[] htmlMessageLines) {
		this(title, Arrays.asList(htmlMessageLines));
	}

	public MessageBox(final String title, final List<String> htmlMessageLines) {
		setText(title);
		setAnimationEnabled(true);
		final Button closeButton = new Button("OK");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final VerticalPanel messageBoxPanel = new VerticalPanel();
		messageBoxPanel.addStyleName("messageBoxPanel");
		for (final String line : htmlMessageLines) {
			messageBoxPanel.add(new HTML(line));
		}
		messageBoxPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		messageBoxPanel.add(closeButton);
		setWidget(messageBoxPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		setModal(true);
		center();
	}
}
