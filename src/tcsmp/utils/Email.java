package tcsmp.utils;

import java.io.Serializable;

public class Email implements Serializable {

	private static final long serialVersionUID = -6663081048156538847L;

	private String to;
	private String from;
	private String subject;
	private String content;

	public Email(String to, String from, String subject, String content) {
		this.to = to;
		this.from = from;
		this.subject = subject;
		this.content = content;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Email [to=" + to + ", from=" + from + ", subject=" + subject + ", content=" + content + "]";
	}
}
