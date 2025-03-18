package com.app.nouapp.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.nouapp.dto.MaterialDto;
import com.app.nouapp.model.Enquiry;
import com.app.nouapp.model.Material;
import com.app.nouapp.model.Response;
import com.app.nouapp.model.StudentInfo;
import com.app.nouapp.service.EnquiryRepository;
import com.app.nouapp.service.MaterialRepository;
import com.app.nouapp.service.ResponseRepository;
import com.app.nouapp.service.StudentInfoRepository;

import ch.qos.logback.core.model.Model;
import jakarta.persistence.criteria.Path;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	StudentInfoRepository srepo;
	
	@Autowired
	ResponseRepository rrepo;
	
	@Autowired
	MaterialRepository mrepo;
	
	@Autowired
	EnquiryRepository erepo;

	@GetMapping("/adminhome")
	public String showAdminHome(HttpSession session, HttpServletResponse response) {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
 			if(session.getAttribute("adminid")!=null) {
				return "/admin/adminhome";
			}
			else 
			{
				return "redirect:/adminlogin";
			}
		}
		catch(Exception ex){
			return "redirect:/adminlogin";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session)
	{
		session.invalidate();
		return "redirect:/adminlogin";
	}
	
	@GetMapping("/managestudents")
	public String showStudents(HttpSession session, HttpServletResponse response,org.springframework.ui.Model model) {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
 			if(session.getAttribute("adminid")!=null) {
 				List<StudentInfo> silist=srepo.findAll();
 				model.addAttribute("silist", silist);
				return "/admin/managestudents";
			}
			else 
			{
				return "redirect:/adminlogin";
			}
		}
		catch(Exception ex){
			return "redirect:/adminlogin";
		}
	}
	
	@GetMapping("/manageenquiries")
	public String showEnquiries(HttpSession session, HttpServletResponse response,org.springframework.ui.Model model) {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
 			if(session.getAttribute("adminid")!=null) {
 				List<Enquiry> enq=erepo.findAll();
 				model.addAttribute("enq",enq);
				return "/admin/manageenquiries";
			}
			else 
			{
				return "redirect:/adminlogin";
			}
		}
		catch(Exception ex){
			return "redirect:/adminlogin";
		}
	}
	
	@GetMapping("/delenq")
	public String showAdminHome(HttpSession session, HttpServletResponse response, @RequestParam int id) {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
 			if(session.getAttribute("adminid")!=null) {
 				Enquiry enq=erepo.getById(id);
 				erepo.delete(enq);
				return "redirect:/admin/manageenquiries";
			}
			else 
			{
				return "redirect:/adminlogin";
			}
		}
		catch(Exception ex){
			return "redirect:/adminlogin";
		}
	}
	
	@GetMapping("/managefeedback")
	public String showFeedback(HttpSession session, HttpServletResponse response,org.springframework.ui.Model model) {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
 			if(session.getAttribute("adminid")!=null) {
 				 List<Response> listfeedback=rrepo.findByResponsetype("feedback");
 				 model.addAttribute("listfeedback",listfeedback);
				return "/admin/managefeedback";
			}
			else 
			{
				return "redirect:/adminlogin";
			}
		}
		catch(Exception ex){
			return "redirect:/adminlogin";
		}
	}
	
	@GetMapping("/managecomplaint")
	public String showComplaint(HttpSession session, HttpServletResponse response,org.springframework.ui.Model model) {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
 			if(session.getAttribute("adminid")!=null) {
 				List<Response> listcomp=rrepo.findByResponsetype("complaint");
 				model.addAttribute("listcomp", listcomp);
				return "/admin/managecomplaint";
			}
			else 
			{
				return "redirect:/adminlogin";
			}
		}
		catch(Exception ex){
			return "redirect:/adminlogin";
		}
	}
	
	@GetMapping("/addstudymaterial")
	public String showAddStudyMaterial(HttpSession session, HttpServletResponse response,org.springframework.ui.Model model) {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
 			if(session.getAttribute("adminid")!=null) {
 				MaterialDto dto=new MaterialDto();
 				model.addAttribute("dto",dto);
				return "/admin/addstudymaterial";
			}
			else 
			{
				return "redirect:/adminlogin";
			}
		}
		catch(Exception ex){
			return "redirect:/adminlogin";
		}
	}
	
	@PostMapping("/addstudymaterial")
	public String addStudyMaterial(HttpSession session, HttpServletResponse response,@ModelAttribute MaterialDto dto, RedirectAttributes attrib) {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
 			if(session.getAttribute("adminid")!=null) {
 				 MultipartFile uploadfile=dto.getUploadfile();
 				 String storageFileName=new Date().getTime()+"_"+uploadfile.getOriginalFilename();
 				 String uploadDir="public/mat/";
 				 java.nio.file.Path uploadPath=Paths.get(uploadDir);
 				 if(!Files.exists(uploadPath)) {
 					 Files.createDirectories(uploadPath);
 				 }
 				 try(InputStream inputStream=uploadfile.getInputStream()){
 					 Files.copy(inputStream, Paths.get(uploadDir+storageFileName),StandardCopyOption.REPLACE_EXISTING);
 				 }
 				 Material m=new Material();
 				 m.setProgram(dto.getProgram());
 				 m.setBranch(dto.getBranch());
 				 m.setYear(dto.getYear()); 
 				 m.setMaterialtype(dto.getMaterialtype());
 				 m.setSubject(dto.getSubject());
 				 m.setTopic(dto.getTopic());
 				 Date dt=new Date();
 				 SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
 				 String posteddate=df.format(dt);
 				 m.setPosteddate(posteddate);
 				 m.setFilename(storageFileName);
 				 mrepo.save(m);
 				 attrib.addFlashAttribute("msg","Material is Uploaded");
				return "redirect:/admin/addstudymaterial";
			}
			else 
			{
				return "redirect:/adminlogin";
			}
		}
		catch(Exception ex){
			return "redirect:/adminlogin";
		}
	}
	
	@GetMapping("/managestudymaterial")
	public String showStudyMaterial(HttpSession session, HttpServletResponse response,org.springframework.ui.Model model) {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
 			if(session.getAttribute("adminid")!=null) {
 				List<Material> mat=mrepo.findAll();
 				model.addAttribute("mat",mat);
				return "/admin/managestudymaterial";
			}
			else 
			{
				return "redirect:/adminlogin";
			}
		}
		catch(Exception ex){
			return "redirect:/adminlogin";
		}
	}
	@GetMapping("/delmat")
	public String delMaterial(HttpSession session, HttpServletResponse response,@RequestParam int id) {
		try {
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
 			if(session.getAttribute("adminid")!=null) {
 				Material m=mrepo.findById(id).get();
 				java.nio.file.Path filePath=Paths.get("public/mat/"+m.getFilename());
 				try {
 					Files.delete(filePath);
 				}
 				catch(Exception e) {
 					e.printStackTrace();
 				}
 				mrepo.delete(m);
				return "redirect:/admin/managestudymaterial";
			}
			else 
			{
				return "redirect:/adminlogin";
			}
		}
		catch(Exception ex){
			return "redirect:/adminlogin";
		}
	}
}
