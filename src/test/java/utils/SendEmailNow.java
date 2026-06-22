package utils;

/**
 * Quick utility to trigger email report manually.
 * Run with: mvn exec:java -Dexec.mainClass=utils.SendEmailNow -Dexec.classpathScope=test
 */
public class SendEmailNow {
    public static void main(String[] args) {
        // Add results from last run
        EmailUtil.addResult("verifyLoginSuccessful", "01 - Login", "PASS", null, null);
        EmailUtil.addResult("verifyUserManagementPageOpens", "02 - User Management", "PASS", null, null);
        EmailUtil.addResult("verifyUserFilterButton", "02 - User Management", "PASS", null, null);
        EmailUtil.addResult("createExpertUser (Jalal Khan Expert)", "02 - User Management", "PASS", null, null);
        EmailUtil.addResult("verifyUserSearchFunctionality", "02 - User Management", "PASS", null, null);
        EmailUtil.addResult("verifyCreatedExpertInList", "02 - User Management", "PASS", null, null);
        EmailUtil.addResult("viewExpertDetails", "02 - User Management", "PASS", null, null);
        EmailUtil.addResult("disableExpertUser (Active -> Inactive)", "02 - User Management", "PASS", null, null);
        EmailUtil.addResult("verifyFarmerManagementPageOpens", "03 - Farmer Management", "PASS", null, null);
        EmailUtil.addResult("verifyFarmerSearchFunctionality", "03 - Farmer Management", "PASS", null, null);
        EmailUtil.addResult("registerFarmerInDICTestAzamgarh", "03 - Farmer Management", "FAIL", "Should redirect after registration. URL stayed on /registration", "screenshots/registerFarmerInDICTestAzamgarh_FAIL_20260622_104459.png");
        EmailUtil.addResult("verifyAdvisoryManagementPageOpens", "04 - Advisory Management", "PASS", null, null);
        EmailUtil.addResult("createAdvisory", "04 - Advisory Management", "FAIL", "Option 'Agriculture' not found in Domain dropdown", "screenshots/createAdvisory_FAIL_20260622_104622.png");
        EmailUtil.addResult("verifyAdvisoryInOutbox", "04 - Advisory Management", "PASS", null, null);
        EmailUtil.addResult("verifyQueryTableDisplayed", "05 - Query Management", "PASS", null, null);
        EmailUtil.addResult("verifyPendingQueryCount (Pending: 10)", "05 - Query Management", "PASS", null, null);
        EmailUtil.addResult("verifyClosedQueryCount (Closed: 10)", "05 - Query Management", "PASS", null, null);
        EmailUtil.addResult("verifyCallManagementPageOpens", "06 - Call Management", "PASS", null, null);
        EmailUtil.addResult("verifyInboundCallTable", "06 - Call Management", "PASS", null, null);
        EmailUtil.addResult("verifyInboundMissedCallCount (Missed: 10)", "06 - Call Management", "PASS", null, null);
        EmailUtil.addResult("verifyInboundClosedCallCount (Closed: 10)", "06 - Call Management", "PASS", null, null);
        EmailUtil.addResult("verifyOutboundCallTable", "06 - Call Management", "PASS", null, null);
        EmailUtil.addResult("verifyOutboundAnsweredCallCount (Answered: 9)", "06 - Call Management", "PASS", null, null);
        EmailUtil.addResult("verifyOutboundUnansweredCallCount (Unanswered: 10)", "06 - Call Management", "PASS", null, null);

        EmailUtil.sendReport(24, 22, 2, 0);
    }
}
