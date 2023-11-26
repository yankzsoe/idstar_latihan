package com.synrgy.commit.controller.dashboard;

import com.synrgy.commit.dao.request.ChangeStatus;
import com.synrgy.commit.dao.request.LoginAdmin;
import com.synrgy.commit.dao.request.SearchEmailDashboard;
import com.synrgy.commit.model.*;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.repository.*;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.service.email.EmailSender;
import com.synrgy.commit.util.EmailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    ReportCommmentRepository reportCommmentRepository;
    @Autowired
    SimplerRepository simplerRepository;
    @Autowired
    KomentarRepository komentarRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    public EmailTemplate emailTemplate;
    @Autowired
    public EmailSender emailSender;
    @Autowired
    ReportUserRepository reportUserRepository;

//    String url = "https://commitapps.herokuapp.com/api/dashboard";
String url = "http://localhost:8081/api/dashboard";
    String success = "alert-success";
    String failed = "alert-danger";

    @GetMapping(value = {"/login"})
    public String index(Model model) {
        model.addAttribute("title", "Dashboard Commit | Login");
        model.addAttribute("url", url);
        model.addAttribute("loginForm", new LoginAdmin());

        return "dashboard-login";
    }

    @RequestMapping(value = "/login", method=RequestMethod.POST)
    public String indexLogin(@ModelAttribute("loginForm") LoginAdmin loginAdmin,
                             HttpSession req, RedirectAttributes redirAttrs) {

        if (loginAdmin.getUsername().equals("admin")  && loginAdmin.getPassword().equals("admin123") ) {
            req.setAttribute("Admin", "Admin");
            return "redirect:/dashboard/report/comment";
        }
        redirAttrs.addFlashAttribute("message", "Username / Password salah!");
        redirAttrs.addFlashAttribute("alerto", failed);
        return "redirect:/dashboard/login";
    }


    @GetMapping(value = {"/report/post"})
    public String ReportPost(Model model, RedirectAttributes redirAttrs, HttpSession req) {
        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }
        List<ReportPost> reportPost = null;
        reportPost = reportRepository.getAllReportPost();
        Long totaldata = 0L;
        totaldata = reportRepository.getLike();
        model.addAttribute("title", "Dashboard Commit | Report Post");
        model.addAttribute("url", url);
        model.addAttribute("reportpost", reportPost);
        model.addAttribute("totaldata", totaldata);
        return "dashboard-report-post";
    }

    @GetMapping(value = {"/report/post/detail/{id_report}"})
    public String ReportPostDetail(Model model, @PathVariable(value = "id_report") Long id_report, RedirectAttributes redirAttrs, HttpSession req) {
        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }

        ReportPost reportpost = reportRepository.getbyID(id_report);
        model.addAttribute("title", "Dashboard Commit | Detail Report Post");
        model.addAttribute("url", url);
        model.addAttribute("reportpost", reportpost);
        return "dashboard-report-post-detail";
    }

    @GetMapping(value = {"/report/post/action/{id_report}"})
    public String ReportReportAction(Model model, @PathVariable(value = "id_report") Long id_report, RedirectAttributes redirAttrs, HttpSession req) {
        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }
        ReportPost reportPost = reportRepository.getbyID(id_report);
        Post post = postRepository.getbyID(reportPost.getId_post().getId_post());

        List<ReportPost> report = reportRepository.getPost(post);
        for (ReportPost repo : report ) {;
            repo.setDeleted_date(new Date());
            reportRepository.save(repo);
        }

        post.setDeleted_date(new Date());
        postRepository.save(post);


        redirAttrs.addFlashAttribute("message", "Post berhasil di delete!");
        redirAttrs.addFlashAttribute("alerto", success);

        return "redirect:/dashboard/report/post";
    }

    @GetMapping(value = {"/report/comment"})
    public String ReportComment(Model model, HttpSession req, RedirectAttributes redirAttrs) {

        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }

        List<ReportComment> reportComment = null;
        reportComment = reportCommmentRepository.getAllReportComment();
        Long totaldata = 0L;
        totaldata = reportCommmentRepository.gettotalReportComment();
        model.addAttribute("title", "Dashboard Commit | Report Comment");
        model.addAttribute("url", url);
        model.addAttribute("reportcomment", reportComment);
        model.addAttribute("totaldata", totaldata);
        return "dashboard-report-comment";
    }

    @GetMapping(value = {"/report/comment/detail/{id_comment}"})
    public String ReportCommentDetail(Model model, @PathVariable(value = "id_comment") Long id_comment, HttpSession req, RedirectAttributes redirAttrs) {
        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }
        ReportComment reportComment = reportCommmentRepository.getbyID(id_comment);
        model.addAttribute("title", "Dashboard Commit | Detail Report Comment");
        model.addAttribute("url", url);
        model.addAttribute("reportcomment", reportComment);
        return "dashboard-report-comment-detail";
    }

    @GetMapping(value = {"/report/comment/action/{id_comment}"})
    public String ReportCommentAction(Model model, @PathVariable(value = "id_comment") Long id_comment, RedirectAttributes redirAttrs, HttpSession req) {
        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }
        ReportComment reportComment = reportCommmentRepository.getbyID(id_comment);
        Komentar komentar = komentarRepository.getbyID(reportComment.getId_komentar().getId_komentar());

        Post checkId = postRepository.getbyID(komentar.getId_post().getId_post());
        checkId.setTotal_komentar(checkId.getTotal_komentar() - 1);
        postRepository.save(checkId);

        List<ReportComment> report = reportCommmentRepository.getComment(komentar);
        for (ReportComment repo : report ) {
            System.out.println(repo);
            repo.setDeleted_date(new Date());
            reportCommmentRepository.save(repo);
        }

        komentar.setDeleted_date(new Date());
        komentarRepository.save(komentar);



        redirAttrs.addFlashAttribute("message", "Komentar berhasil di delete!");
        redirAttrs.addFlashAttribute("alerto", success);

        return "redirect:/dashboard/report/comment";
    }

    @GetMapping(value = {"/report/user"})
    public String ReportUser(Model model, RedirectAttributes redirAttrs, HttpSession req) {
        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }
        List<ReportUser> reportUsers = null;
        reportUsers = reportUserRepository.getAllReportUser();
        Long totaldata = 0L;
        totaldata = reportUserRepository.getLike();
        model.addAttribute("title", "Dashboard Commit | Report User");
        model.addAttribute("url", url);
        model.addAttribute("reportusers", reportUsers);
        model.addAttribute("totaldata", totaldata);
        return "dashboard-report-user";
    }

    @GetMapping(value = {"/report/user/detail/{id_report_user}"})
    public String ReportUserDetail(Model model, @PathVariable(value = "id_report_user") Long id_report_user, HttpSession req, RedirectAttributes redirAttrs) {
        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }
        ReportUser reportUser = reportUserRepository.getbyID(id_report_user);
        model.addAttribute("title", "Dashboard Commit | Detail Report Comment");
        model.addAttribute("url", url);
        model.addAttribute("reportusers", reportUser);
        return "dashboard-report-user-detail";
    }

    @GetMapping(value = {"/report/user/action/{id_user}"})
    public String ReportUserAction(Model model, @PathVariable(value = "id_user") Long id_user, RedirectAttributes redirAttrs, HttpSession req) {
        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }
        User user = userRepository.getbyID(id_user);
        List<ReportUser> reportUser = reportUserRepository.getListUser(user);

        user.setBlocked(true);

        userRepository.save(user);

        for (ReportUser repo : reportUser ) {
            System.out.println(repo);
            repo.setDeleted_date(new Date());
            reportUserRepository.save(repo);
        }

        redirAttrs.addFlashAttribute("message", "User berhasil di block!");
        redirAttrs.addFlashAttribute("alerto", success);

        return "redirect:/dashboard/report/user";
    }

    @GetMapping(value = {"/simpler-payment"})
    public String SimplerPayment(Model model, RedirectAttributes redirAttrs, HttpSession req) {
        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }
        List<SimplerPayment> simplerPayments = null;
        simplerPayments = simplerRepository.getOnprogressPayment("On Progress");
        Long totaldata = 0L;
        totaldata = simplerRepository.getTotalSimpler("On Progress");
        model.addAttribute("title", "Dashboard Commit | Simpler Payment");
        model.addAttribute("url", url);
        model.addAttribute("simplerpayment", simplerPayments);
        model.addAttribute("totaldata", totaldata);
        return "dashboard-simpler";
    }

    @GetMapping(value = {"/simpler-payment/detail/{id_payment}"})
    public String SimplerPaymentDetail(Model model, @PathVariable(value = "id_payment") Long id_payment, RedirectAttributes redirAttrs, HttpSession req) {
        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }
        SimplerPayment simplerPayments = simplerRepository.getbyID(id_payment);
        model.addAttribute("title", "Dashboard Commit | Detail Simpler Payment");
        model.addAttribute("url", url);
        model.addAttribute("simplerpayment", simplerPayments);
        return "dashboard-simpler-detail";
    }

    @GetMapping(value = {"/simpler-payment/approve/{id_payment}"})
    public String SimplerPaymentApprove(Model model, @PathVariable(value = "id_payment") Long id_payment, RedirectAttributes redirAttrs, HttpSession req) {
        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }
        SimplerPayment simplerPayments = simplerRepository.getbyID(id_payment);
        User user = userRepository.getbyID(simplerPayments.getId_user().getId());
        String template = emailTemplate.ApproveEmail();

        user.setStatus("Subscribed");
        Date dateNow = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateNow);
        calendar.add(Calendar.MONTH, Math.toIntExact(simplerPayments.getPlan()));
        Date expirationDate = calendar.getTime();

        user.setExpiredSubs(expirationDate);
        userRepository.save(user);
        simplerPayments.setStatus("Success");
        simplerRepository.save(simplerPayments);
        model.addAttribute("url", url);
        model.addAttribute("simplerpayment", simplerPayments);

        template = template.replaceAll("\\{\\{USERNAME}}", (simplerPayments.getId_user().getFullname() == null ? "" +
                "@UserName"
                :
                "" + simplerPayments.getId_user().getFullname()));
        template = template.replaceAll("\\{\\{TRANSACTION}}", simplerPayments.getTransaction_id());
        template = template.replaceAll("\\{\\{PLAN}}", String.valueOf(simplerPayments.getPlan()));
        template = template.replaceAll("\\{\\{STATUS}}", simplerPayments.getStatus());
        template = template.replaceAll("\\{\\{PAYMENT}}", simplerPayments.getPayment_method());
        template = template.replaceAll("\\{\\{TOTAL}}", simplerPayments.getTotal_paid());

        emailSender.sendAsync(simplerPayments.getId_user().getUsername(), "Absensi - Simpler Payment", template);

        redirAttrs.addFlashAttribute("message", "Payment berhasil di Approve!");
        redirAttrs.addFlashAttribute("alerto", success);

        return "redirect:/dashboard/simpler-payment";
    }

    @GetMapping(value = {"/simpler-payment/reject/{id_payment}"})
    public String SimplerPaymentReject(Model model, @PathVariable(value = "id_payment") Long id_payment, RedirectAttributes redirAttrs, HttpSession req) {
        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }
        SimplerPayment simplerPayments = simplerRepository.getbyID(id_payment);
        User user = userRepository.getbyID(simplerPayments.getId_user().getId());
        String template = emailTemplate.RejectEmail();

        simplerPayments.setStatus("Failed");
        simplerRepository.save(simplerPayments);
        model.addAttribute("url", url);
        model.addAttribute("simplerpayment", simplerPayments);

        template = template.replaceAll("\\{\\{USERNAME}}", (simplerPayments.getId_user().getFullname() == null ? "" +
                "@UserName"
                :
                "" + simplerPayments.getId_user().getFullname()));

        emailSender.sendAsync(simplerPayments.getId_user().getUsername(), "Absensi - Simpler Payment", template);

        redirAttrs.addFlashAttribute("message", "Payment berhasil di Reject!");
        redirAttrs.addFlashAttribute("alerto", success);

        return "redirect:/dashboard/simpler-payment";
    }

    @GetMapping(value = {"/verify-user"})
    public String verifyUser(Model model, RedirectAttributes redirAttrs, HttpSession req) {
        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }

        model.addAttribute("title", "Dashboard Commit | Verify User");
        model.addAttribute("url", url);
        model.addAttribute("searchEmail", new SearchEmailDashboard());
        model.addAttribute("changeStatus", new ChangeStatus());

        return "dashboard-verify-user";
    }

    @RequestMapping(value = "/verify-user", method=RequestMethod.POST)
    public String verifyUserSearch(@ModelAttribute("searchEmail") SearchEmailDashboard searchEmailDashboard,
                             HttpSession req, RedirectAttributes redirAttrs, Model model) {
        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }

        User user = userRepository.findEmailUser(searchEmailDashboard.getEmail(), "User");
        model.addAttribute("title", "Dashboard Commit | Verify User");
        model.addAttribute("url", url);
        model.addAttribute("detail",user);

        return "dashboard-verify-user";
    }

    @GetMapping(value = "/verify-user/action/{id_user}")
    public String verifyUserAction(HttpSession req, RedirectAttributes redirAttrs, Model model,
                                   @PathVariable(value = "id_user") Long id_user) {
        if (req.getAttribute("Admin") == null) {
            redirAttrs.addFlashAttribute("message", "Login terlebih dahulu!");
            redirAttrs.addFlashAttribute("alerto", failed);
            return "redirect:/dashboard/login";
        }
        String template = emailTemplate.verifyUser();

        User user = userRepository.getbyID(id_user);

        user.setStatus("Verified");

        userRepository.save(user);
        template = template.replaceAll("\\{\\{USERNAME}}", user.getFullname());

        emailSender.sendAsync(user.getUsername(), "Absensi - Verified Account", template);


        model.addAttribute("title", "Dashboard Commit | Verify User");
        model.addAttribute("url", url);
        redirAttrs.addFlashAttribute("message", "Status Berhasil diubah!");
        redirAttrs.addFlashAttribute("alerto", success);

        return "redirect:/dashboard/verify-user";
    }

    @GetMapping(value = {"/logout"})
    public String indexLogout(Model model, HttpSession req, RedirectAttributes redirAttrs) {
        req.removeAttribute("Admin");
        redirAttrs.addFlashAttribute("message", "Logout Berhasil!");
        redirAttrs.addFlashAttribute("alerto", success);
        return "redirect:/dashboard/login";
    }
}
