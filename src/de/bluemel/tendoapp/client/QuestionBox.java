package de.bluemel.tendoapp.client;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class QuestionBox extends DialogBox {

	private YesNoHandler yesNoHandler;

	public QuestionBox(final String title, final String htmlMessageLine, final YesNoHandler yesNoHandler) {
		this(title, Arrays.asList(new String[] { htmlMessageLine }), yesNoHandler);
	}

	public QuestionBox(final String title, final String[] htmlMessageLines, final YesNoHandler yesNoHandler) {
		this(title, Arrays.asList(htmlMessageLines), yesNoHandler);
	}

	public QuestionBox(final String title, final List<String> htmlMessageLines, final YesNoHandler yesNoHandler) {
		this.yesNoHandler = yesNoHandler;
		setText(title);
		setAnimationEnabled(true);
		final Button yesButton = new Button("Ja");
		yesButton.getElement().setId("yesButton");
		final Button noButton = new Button("Nein");
		noButton.getElement().setId("noButton");
		final VerticalPanel questionBoxPanel = new VerticalPanel();
		final HorizontalPanel questionBoxButtonPanel = new HorizontalPanel();
		questionBoxPanel.addStyleName("questionBoxPanel");
		for (final String line : htmlMessageLines) {
			questionBoxPanel.add(new HTML(line));
		}
		questionBoxPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		questionBoxButtonPanel.setSpacing(10);
		questionBoxButtonPanel.add(yesButton);
		questionBoxButtonPanel.add(noButton);
		questionBoxPanel.add(questionBoxButtonPanel);
		setWidget(questionBoxPanel);
		if (this.yesNoHandler == null) {
			throw new IllegalArgumentException("No yes / no handler given");
		}
		yesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				yesNoHandler.onYes();
				hide();
			}
		});
		noButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				yesNoHandler.onNo();
				hide();
			}
		});
		setModal(true);
		center();
	}
}
