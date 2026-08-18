package utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * EmailUtil - Sends professional colourful HTML test report via email.
 * Includes: Project info, QA name, pass/fail/skip counts, test case table,
 * and embedded screenshots for FAIL/SKIP cases.
 */
public class EmailUtil {

    // Store test results for email
    private static final List<TestResult> results = Collections.synchronizedList(new ArrayList<>());

    public static void addResult(String testName, String suiteName, String status, String reason, String screenshotPath) {
        results.add(new TestResult(testName, suiteName, status, reason, screenshotPath));
    }

    public static void sendReport(int totalTests, int passed, int failed, int skipped) {
        String enabled = ConfigReader.getProperty("email.enabled");
        if (!"true".equalsIgnoreCase(enabled)) {
            System.out.println(">> Email disabled. Skipping.");
            return;
        }

        // Only share the report when more than 30 test cases have passed.
        if (passed <= 30) {
            System.out.println(">> Only " + passed + " test(s) passed (need > 30). Email NOT sent.");
            return;
        }

        String host = ConfigReader.getProperty("email.smtp.host");
        String port = ConfigReader.getProperty("email.smtp.port");
        String sender = ConfigReader.getProperty("email.sender");
        String password = ConfigReader.getProperty("email.password");
        String recipient = ConfigReader.getProperty("email.recipient");
        String cc = ConfigReader.getProperty("email.cc");

        String environment = ConfigReader.getProperty("email.environment");
        if (environment == null || environment.isEmpty()) {
            environment = "Dev";
        }
        String qaName = ConfigReader.getProperty("email.qa");
        if (qaName == null || qaName.isEmpty()) {
            qaName = "Jalal Khan";
        }

        String overallStatus = (failed > 0) ? "FAILED" : (skipped == totalTests) ? "SKIPPED (Service Down)" : "PASSED";
        String statusColor = (failed > 0) ? "#e74c3c" : (skipped == totalTests) ? "#f39c12" : "#27ae60";

        String subject = "Automation Script Result - Kisan Sarathi 2.0 | " + overallStatus
                + " | Pass:" + passed + " Fail:" + failed + " Skip:" + skipped;

        String timestamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(new Date());

        StringBuilder body = new StringBuilder();
        body.append("<html><head><style>");
        body.append("body { font-family: 'Segoe UI', Arial, sans-serif; background: #f5f6fa; margin: 0; padding: 20px; }");
        body.append(".container { max-width: 900px; margin: 0 auto; background: #fff; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); overflow: hidden; }");
        body.append(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; }");
        body.append(".header h1 { margin: 0; font-size: 24px; }");
        body.append(".header p { margin: 5px 0 0; opacity: 0.9; font-size: 14px; }");
        body.append(".info-bar { background: #f8f9fa; padding: 15px 30px; display: flex; border-bottom: 1px solid #e9ecef; }");
        body.append(".info-item { flex: 1; }");
        body.append(".info-label { font-size: 11px; color: #6c757d; text-transform: uppercase; letter-spacing: 1px; }");
        body.append(".info-value { font-size: 14px; font-weight: 600; color: #2d3436; margin-top: 3px; }");
        body.append(".stats { display: flex; padding: 20px 30px; gap: 15px; }");
        body.append(".stat-card { flex: 1; text-align: center; padding: 15px; border-radius: 8px; }");
        body.append(".stat-total { background: #e8f4fd; border: 1px solid #bee5eb; }");
        body.append(".stat-pass { background: #d4edda; border: 1px solid #c3e6cb; }");
        body.append(".stat-fail { background: #f8d7da; border: 1px solid #f5c6cb; }");
        body.append(".stat-skip { background: #fff3cd; border: 1px solid #ffc107; }");
        body.append(".stat-number { font-size: 28px; font-weight: 700; }");
        body.append(".stat-label { font-size: 11px; text-transform: uppercase; letter-spacing: 1px; margin-top: 5px; }");
        body.append(".status-badge { display: inline-block; padding: 6px 16px; border-radius: 20px; color: white; font-weight: 600; font-size: 13px; margin-top: 10px; }");
        body.append("table { width: 100%; border-collapse: collapse; margin: 0; }");
        body.append("th { background: #343a40; color: white; padding: 12px 15px; text-align: left; font-size: 12px; text-transform: uppercase; letter-spacing: 0.5px; }");
        body.append("td { padding: 10px 15px; border-bottom: 1px solid #e9ecef; font-size: 13px; }");
        body.append("tr:nth-child(even) { background: #f8f9fa; }");
        body.append(".pass { color: #27ae60; font-weight: 600; }");
        body.append(".fail { color: #e74c3c; font-weight: 600; }");
        body.append(".skip { color: #f39c12; font-weight: 600; }");
        body.append(".section-title { padding: 20px 30px 10px; font-size: 16px; font-weight: 600; color: #2d3436; border-top: 3px solid #667eea; }");
        body.append(".footer { background: #f8f9fa; padding: 15px 30px; text-align: center; font-size: 11px; color: #6c757d; border-top: 1px solid #e9ecef; }");
        body.append(".screenshot-section { padding: 15px 30px; }");
        body.append(".screenshot-item { margin-bottom: 15px; padding: 10px; background: #fff5f5; border: 1px solid #f5c6cb; border-radius: 8px; }");
        body.append(".screenshot-item h4 { margin: 0 0 8px; color: #e74c3c; font-size: 13px; }");
        body.append("</style></head><body>");

        // Container
        body.append("<div class='container'>");

        // Header
        body.append("<div class='header'>");
        body.append("<h1>🧪 Automation Script Result</h1>");
        body.append("<p>Kisan Sarathi 2.0 - Admin Panel</p>");
        body.append("</div>");

        // Info bar
        body.append("<table style='width:100%;background:#f8f9fa;border-bottom:1px solid #e9ecef;'><tr>");
        body.append("<td style='padding:15px 30px;border:none;'><span style='font-size:11px;color:#6c757d;'>PROJECT</span><br><strong>Kisan Sarathi 2.0</strong></td>");
        body.append("<td style='padding:15px 30px;border:none;'><span style='font-size:11px;color:#6c757d;'>QA ENGINEER</span><br><strong>").append(qaName).append("</strong></td>");
        body.append("<td style='padding:15px 30px;border:none;'><span style='font-size:11px;color:#6c757d;'>EXECUTION DATE</span><br><strong>").append(timestamp).append("</strong></td>");
        body.append("<td style='padding:15px 30px;border:none;'><span style='font-size:11px;color:#6c757d;'>ENVIRONMENT</span><br><strong>").append(environment).append("</strong></td>");
        body.append("</tr></table>");

        // Stats cards
        body.append("<table style='width:100%;padding:20px 30px;'><tr>");
        body.append("<td style='text-align:center;padding:15px;background:#e8f4fd;border-radius:8px;border:none;'><div style='font-size:28px;font-weight:700;color:#0c5460;'>").append(totalTests).append("</div><div style='font-size:11px;color:#0c5460;text-transform:uppercase;'>TOTAL</div></td>");
        body.append("<td style='width:15px;border:none;'></td>");
        body.append("<td style='text-align:center;padding:15px;background:#d4edda;border-radius:8px;border:none;'><div style='font-size:28px;font-weight:700;color:#155724;'>").append(passed).append("</div><div style='font-size:11px;color:#155724;text-transform:uppercase;'>PASSED</div></td>");
        body.append("<td style='width:15px;border:none;'></td>");
        body.append("<td style='text-align:center;padding:15px;background:#f8d7da;border-radius:8px;border:none;'><div style='font-size:28px;font-weight:700;color:#721c24;'>").append(failed).append("</div><div style='font-size:11px;color:#721c24;text-transform:uppercase;'>FAILED</div></td>");
        body.append("<td style='width:15px;border:none;'></td>");
        body.append("<td style='text-align:center;padding:15px;background:#fff3cd;border-radius:8px;border:none;'><div style='font-size:28px;font-weight:700;color:#856404;'>").append(skipped).append("</div><div style='font-size:11px;color:#856404;text-transform:uppercase;'>SKIPPED</div></td>");
        body.append("</tr></table>");

        // Test Cases Table
        body.append("<div class='section-title'>📋 Test Case Results</div>");
        body.append("<table><tr><th>#</th><th>Suite</th><th>Test Case</th><th>Status</th><th>Reason</th></tr>");

        int idx = 1;
        for (TestResult r : results) {
            String statusClass = r.status.equals("PASS") ? "pass" : r.status.equals("FAIL") ? "fail" : "skip";
            String icon = r.status.equals("PASS") ? "✅" : r.status.equals("FAIL") ? "❌" : "⚠️";
            String reason = (r.reason != null && !r.reason.isEmpty()) ? r.reason : "-";
            if (reason.length() > 80) reason = reason.substring(0, 80) + "...";

            body.append("<tr><td>").append(idx++).append("</td>");
            body.append("<td>").append(r.suiteName).append("</td>");
            body.append("<td>").append(r.testName).append("</td>");
            body.append("<td class='").append(statusClass).append("'>").append(icon).append(" ").append(r.status).append("</td>");
            body.append("<td style='font-size:11px;color:#6c757d;'>").append(reason).append("</td></tr>");
        }
        body.append("</table>");

        // Footer
        body.append("<div class='footer'>");
        body.append("📧 Automated Report | Kisan Sarathi 2.0 Admin Panel Automation Framework | QA: ").append(qaName);
        body.append("</div>");

        body.append("</div></body></html>");

        // Send email
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            if (cc != null && !cc.isEmpty()) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            }
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();

            // HTML body
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(body.toString(), "text/html; charset=utf-8");
            multipart.addBodyPart(bodyPart);

            // Attach HTML report
            File reportFile = new File("reports/TestReport.html");
            if (reportFile.exists()) {
                MimeBodyPart attachPart = new MimeBodyPart();
                attachPart.attachFile(reportFile);
                attachPart.setFileName("TestReport.html");
                multipart.addBodyPart(attachPart);
            }

            // Attach screenshots for FAIL/SKIP
            File screenshotDir = new File("screenshots");
            if (screenshotDir.exists() && screenshotDir.isDirectory()) {
                File[] screenshots = screenshotDir.listFiles((dir, name) ->
                        name.contains("FAIL") || name.contains("SKIP"));
                if (screenshots != null) {
                    // Limit to 5 screenshots to keep email size manageable
                    int count = 0;
                    for (File ss : screenshots) {
                        if (count >= 5) break;
                        MimeBodyPart ssPart = new MimeBodyPart();
                        ssPart.attachFile(ss);
                        ssPart.setFileName(ss.getName());
                        multipart.addBodyPart(ssPart);
                        count++;
                    }
                }
            }

            message.setContent(multipart);
            Transport.send(message);
            System.out.println(">> EMAIL SENT to " + recipient + " | Subject: " + subject);

        } catch (Exception e) {
            System.out.println(">> EMAIL FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Inner class to hold test results
    static class TestResult {
        String testName;
        String suiteName;
        String status;
        String reason;
        String screenshotPath;

        TestResult(String testName, String suiteName, String status, String reason, String screenshotPath) {
            this.testName = testName;
            this.suiteName = suiteName;
            this.status = status;
            this.reason = reason;
            this.screenshotPath = screenshotPath;
        }
    }
}
