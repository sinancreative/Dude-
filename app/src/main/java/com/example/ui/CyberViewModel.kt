package com.example.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.Inquiry
import com.example.data.InquiryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class Course(
    val id: String,
    val title: String,
    val shortDesc: String,
    val longDesc: String,
    val topics: List<String>,
    val tag: String,
    val iconName: String
)

data class Testimonial(
    val name: String,
    val role: String,
    val feedback: String,
    val rating: Int = 5
)

data class FAQItem(
    val id: Int,
    val question: String,
    val answer: String
)

class CyberViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InquiryRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = InquiryRepository(database.inquiryDao())
    }

    // Reactive inquiries list from local Database
    val inquiries: StateFlow<List<Inquiry>> = repository.allInquiries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Form Fields State
    var nameState by mutableStateOf("")
    var phoneState by mutableStateOf("")
    var emailState by mutableStateOf("")
    var messageState by mutableStateOf("")
    var selectedCourseState by mutableStateOf("AI-Powered Digital Marketing")
    
    var isSubmitting by mutableStateOf(false)
    var showFormSuccessDialog by mutableStateOf(false)
    var formErrorMessage by mutableStateOf<String?>(null)

    // Selected Course for Details BottomSheet/Dialog
    var selectedCourseForDetails by mutableStateOf<Course?>(null)

    // Testimonial Carousel Auto-cycle
    var selectedTestimonialIndex by mutableStateOf(0)

    // Expandable states for FAQs (stores [FAQ id] -> isExpanded)
    val expandedFaqMap = mutableStateMapOf<Int, Boolean>().apply {
        put(1, false)
        put(2, false)
        put(3, false)
        put(4, false)
        put(5, false)
        put(6, false)
    }

    // static data fields
    val courses = listOf(
        Course(
            id = "dm",
            title = "AI-Powered Digital Marketing",
            shortDesc = "Master SEO, social media, lead generation, and revolutionary AI workflow automation.",
            longDesc = "This state-of-the-art program equips you with advanced Digital Marketing strategies supercharged by generative AI. You will learn to write copy, optimize search engines, execute live PPC campaigns, and automate customer pipelines like a global 2026 marketer.",
            tag = "Most Popular",
            topics = listOf(
                "Search Engine Optimization (SEO)",
                "Social Media Marketing & Strategy",
                "Google Ads & PPC Campaigns",
                "Content Marketing & Copywriting",
                "Email Marketing & Funnels",
                "AI Marketing Tools & Automations",
                "Analytics, ROI & Performance Reporting"
            ),
            iconName = "trending_up"
        ),
        Course(
            id = "vid",
            title = "Professional Videography & Content Creation",
            shortDesc = "Learn photography, video shooting, filmmaking editing, Reels, and storytelling.",
            longDesc = "Turn your creative vision into commercial-grade media assets. Gain deep hands-on expertise with DSLR/Mirrorless cameras, lighting grids, field audio devices, and professional NLE software to capture and produce stunning content for brands, YouTube, and screens.",
            tag = "Creative Hotspot",
            topics = listOf(
                "Professional Videography & DSLR Handling",
                "Studio & Outdoor Photography",
                "Dynamic Video Editing (Premiere/DaVinci)",
                "Visual Storytelling & Narrative Scripting",
                "YouTube Content Creation strategies",
                "Instagram Reels Production & Viral Hooks",
                "Commercial & Brand Content Production"
            ),
            iconName = "videocam"
        ),
        Course(
            id = "ecom",
            title = "E-Commerce & Online Business",
            shortDesc = "Build, brand, market, and manage highly profitable Shopify and WooCommerce stores.",
            longDesc = "The complete guide to launching and scaling online storefronts. From selecting global high-demand niches and setting up automated warehouses to integrating secure payment systems, launching high-performance sales funnels, and acquiring customers digitally.",
            tag = "Career Accelerator",
            topics = listOf(
                "Shopify Store Design & Theme Alignment",
                "WooCommerce & WordPress Customization",
                "A-Z Online Store Setup & Logistics",
                "Product Branding & Premium Packaging Design",
                "Digital Sales Funnels & Lead Capture",
                "Customer Acquisition & Retargeting",
                "Operations & Business Automation systems"
            ),
            iconName = "shopping_cart"
        ),
        Course(
            id = "eng",
            title = "Spoken English & Communication",
            shortDesc = "Perfect your grammar, business speech, career interviews, and public presentation skills.",
            longDesc = "Unlock new career doors by mastering the world's business language. This practical boot camp focuses on intensive interactive speaking, interview simulation, accent neuralization, and corporate dynamic communication to make you stand out at global hiring firms.",
            tag = "Essential Skill",
            topics = listOf(
                "Spoken English Fluency & Dynamic Idioms",
                "Executive Business Communication",
                "High-Impact Public Speaking & Stage Presence",
                "Mock HR & Professional Interview Preparation",
                "Personality Development & Soft Skills",
                "Workplace Communication & Professional Emailing"
            ),
            iconName = "record_voice_over"
        )
    )

    val testimonials = listOf(
        Testimonial(
            name = "Aswanth K.",
            role = "Digital Marketing Student",
            feedback = "The AI tools training here is next-level! I set up an ad campaign that got real conversions during the class. I got placed as a digital strategist right after finishing."
        ),
        Testimonial(
            name = "Shaheen P.",
            role = "Videography Student",
            feedback = "Coming into CyberWise, I only knew basic phone photos. Now I can work with DSLR cameras, edit cinematic sequences, and shoot commercial brand campaigns confidently."
        ),
        Testimonial(
            name = "Fathima Anjali",
            role = "Spoken English Student",
            feedback = "This program completely broke my hesitation. Public speaking exercises and mock interviews helped me build the fluency and personality to crack corporate interviews!"
        )
    )

    val faqs = listOf(
        FAQItem(
            id = 1,
            question = "What courses are available?",
            answer = "CyberWise Skillversity offers four primary tracks: AI-Powered Digital Marketing, Professional Videography & Content Creation, E-Commerce & Online Business, and Spoken English & Communication, along with UGC-recognized academic degree pathways."
        ),
        FAQItem(
            id = 2,
            question = "Are certificates provided?",
            answer = "Yes! Upon successful completion of your course and live projects, you are awarded an industry-accredited Professional Certification. If you pursue a degree pathway, you will receive a UGC-recognized degree certificate."
        ),
        FAQItem(
            id = 3,
            question = "Do you offer placement assistance?",
            answer = "Absolutely. We have 100+ career placements and provide dedicated placement support, pre-interview screening, resume building, and direct mock interview preparation."
        ),
        FAQItem(
            id = 4,
            question = "Are internships available?",
            answer = "Yes, every student undergoes practical training and has direct access to live industry projects and hands-on internship opportunities at local partner agencies."
        ),
        FAQItem(
            id = 5,
            question = "Can I pursue a degree alongside skill programs?",
            answer = "Yes! CyberWise integrates skill-based education with UGC-recognized degree pathways, enabling students to gain practical expertise alongside accredited academic qualifications."
        ),
        FAQItem(
            id = 6,
            question = "Is the training practical?",
            answer = "100% practical. We believe in learning by doing. No dry textbook theories—you will develop real portfolios, build real Shopify stores, manage real ad campaigns, and direct real professional corporate video content."
        )
    )

    fun toggleFaq(id: Int) {
        val current = expandedFaqMap[id] ?: false
        expandedFaqMap[id] = !current
    }

    fun submitInquiry() {
        if (nameState.isBlank()) {
            formErrorMessage = "Please enter your name"
            return
        }
        if (phoneState.isBlank()) {
            formErrorMessage = "Please enter your phone number"
            return
        }
        if (emailState.isBlank() || !emailState.contains("@")) {
            formErrorMessage = "Please enter a valid email address"
            return
        }

        formErrorMessage = null
        isSubmitting = true

        viewModelScope.launch {
            try {
                val inquiry = Inquiry(
                    name = nameState.trim(),
                    phone = phoneState.trim(),
                    email = emailState.trim(),
                    courseInterested = selectedCourseState,
                    message = messageState.trim()
                )
                repository.insert(inquiry)

                // Reset state
                nameState = ""
                phoneState = ""
                emailState = ""
                messageState = ""
                isSubmitting = false
                showFormSuccessDialog = true
            } catch (e: Exception) {
                isSubmitting = false
                formErrorMessage = "Submission failed: ${e.message}. Please try again."
            }
        }
    }

    fun deleteInquiry(id: Int) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    fun nextTestimonial() {
        selectedTestimonialIndex = (selectedTestimonialIndex + 1) % testimonials.size
    }

    fun prevTestimonial() {
        selectedTestimonialIndex = if (selectedTestimonialIndex == 0) {
            testimonials.size - 1
        } else {
            selectedTestimonialIndex - 1
        }
    }
}
