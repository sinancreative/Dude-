package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.Inquiry
import com.example.ui.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CyberDarkPurple
                ) {
                    CyberWiseApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CyberWiseApp(viewModel: CyberViewModel = viewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    
    // Database states
    val inquiries by viewModel.inquiries.collectAsState()
    
    // Active drawer sheet state
    var showInquiryDrawer by remember { mutableStateOf(false) }

    // Glow effects background brush
    val radialBackgroundBrush = Brush.radialGradient(
        colors = listOf(Color(0xFF2C0A5F), CyberDarkPurple),
        radius = 2200f
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_logo),
                            contentDescription = "CyberWise Logo",
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, CyberNeonPurple, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Column {
                            Text(
                                text = "CyberWise",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberGlowingWhite
                            )
                            Text(
                                text = "SKILLVERSITY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CyberNeonPurple,
                                letterSpacing = 1.5.sp
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showInquiryDrawer = true },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("inquiry_history_icon")
                    ) {
                        BadgedBox(
                            badge = {
                                if (inquiries.isNotEmpty()) {
                                    Badge(
                                        containerColor = CyberAccentPink,
                                        contentColor = Color.White
                                    ) {
                                        Text(inquiries.size.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "View Inquiries History",
                                tint = CyberGlowingWhite
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CyberDarkPurple.copy(alpha = 0.95f),
                    titleContentColor = CyberGlowingWhite
                ),
                modifier = Modifier.border(
                    BorderStroke(1.dp, Brush.verticalGradient(listOf(CyberGlassBorder, Color.Transparent))),
                    RoundedCornerShape(0.dp)
                )
            )
        },
        containerColor = CyberDarkPurple,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    try {
                        val whatsappIntent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://api.whatsapp.com/send?phone=918590348905&text=Hello%20CyberWise%20Skillversity!%20I%20am%20interested%20in%20courses%20or%20academic%20counseling.")
                        }
                        context.startActivity(whatsappIntent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "WhatsApp is not installed. Contact +91 85903 48905", Toast.LENGTH_LONG).show()
                    }
                },
                containerColor = Color(0xFF25D366),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 16.dp, end = 8.dp)
                    .testTag("whatsapp_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_whatsapp),
                        contentDescription = "WhatsApp Chat",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Chat with Us",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(radialBackgroundBrush)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
                    .widthIn(max = 850.dp), // Responsive adaptive constraint for foldables and tablets
                contentPadding = PaddingValues(bottom = 40.dp)
            ) {
                
                // 1. HERO SECTION
                item {
                    HeroSection(
                        onExploreCoursesIndex = {
                            coroutineScope.launch {
                                // Animated scroll to courses label
                                listState.animateScrollToItem(2)
                            }
                        },
                        onBookCounselingIndex = {
                            coroutineScope.launch {
                                // Animated scroll to registration form
                                listState.animateScrollToItem(10)
                            }
                        }
                    )
                }

                // 2. STATISTICS SECTION
                item {
                    StatisticsSection()
                }

                // 3. COURSE CATALOG SECTION HEADLINE
                item {
                    SectionHeader(
                        title = "Future-Ready Academic Specializations",
                        subtitle = "Our Industry-Demanded Courses"
                    )
                }

                // 4. COURSE CARDS LIST
                items(viewModel.courses) { course ->
                    CourseCard(
                        course = course,
                        onLearnMore = {
                            viewModel.selectedCourseForDetails = course
                        },
                        onQuickRegister = {
                            viewModel.selectedCourseState = course.title
                            coroutineScope.launch {
                                listState.animateScrollToItem(10)
                            }
                            Toast.makeText(context, "Selected ${course.title}! Scroll down to complete registration.", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                // 5. DEGREE PATHWAYS SECTION
                item {
                    SectionHeader(
                        title = "UGC Recognized Academic Programs",
                        subtitle = "Integrate Accredited Degrees with Skill Practice"
                    )
                }
                item {
                    DegreePathwaysSection()
                }

                // 6. WHY CHOOSE US
                item {
                    SectionHeader(
                        title = "Why Choose CyberWise Skillversity",
                        subtitle = "We Bridge the Gap Between Education & Industry"
                    )
                }
                item {
                    WhyChooseUsSection()
                }

                // 7. LEARNING PROCESS
                item {
                    SectionHeader(
                        title = "Our Proven Graduate Success Path",
                        subtitle = "The CyberWise Career Lifecycle"
                    )
                }
                item {
                    LearningProcessSection()
                }

                // 8. TESTIMONIALS SLIDER SECTION
                item {
                    SectionHeader(
                        title = "Voice of our Achievers",
                        subtitle = "Student Testimonials"
                    )
                }
                item {
                    TestimonialSlider(viewModel = viewModel)
                }

                // 9. FAQ ACCORDION SECTION
                item {
                    SectionHeader(
                        title = "Frequently Answered Questions",
                        subtitle = "Have Doubts? We Got You Covered"
                    )
                }
                item {
                    FaqAccordionList(viewModel = viewModel)
                }

                // 10. ABOUT US SECTION
                item {
                    SectionHeader(
                        title = "About CyberWise Skillversity",
                        subtitle = "Who We Are & What Drives Us"
                    )
                }
                item {
                    AboutUsSection()
                }

                // 11. CONTACT & REGISTRATION FORM
                item {
                    SectionHeader(
                        title = "Start Your Career Journey Today",
                        subtitle = "Book Your Free 1-on-1 Counseling Seat"
                    )
                }
                item {
                    ContactFormSection(viewModel = viewModel)
                }

                // 12. LOCAL RECENT INQUIRIES LINK
                if (inquiries.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .clickable { showInquiryDrawer = true },
                            colors = CardDefaults.cardColors(containerColor = CyberGlassBg),
                            border = BorderStroke(1.dp, CyberGlassBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Success",
                                        tint = CyberAccentPink
                                    )
                                    Column {
                                        Text(
                                            text = "You have ${inquiries.size} active counseling request(s)",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CyberGlowingWhite
                                        )
                                        Text(
                                            text = "Tap to review submission status & timelines",
                                            fontSize = 11.sp,
                                            color = CyberLightGray
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Open Inquiries",
                                    tint = CyberNeonPurple
                                )
                            }
                        }
                    }
                }

                // 13. FOOTER SECTION
                item {
                    FooterSection(
                        onScrollToSection = { index ->
                            coroutineScope.launch {
                                listState.animateScrollToItem(index)
                            }
                        }
                    )
                }
            }
        }
    }

    // --- DETAILED COURSE DIALOG / SHEET ---
    viewModel.selectedCourseForDetails?.let { course ->
        CourseDetailsDialog(
            course = course,
            onDismiss = { viewModel.selectedCourseForDetails = null },
            onEnroll = {
                viewModel.selectedCourseState = course.title
                viewModel.selectedCourseForDetails = null
                coroutineScope.launch {
                    listState.animateScrollToItem(10)
                }
                Toast.makeText(context, "Selected ${course.title}! Fill out constraints below.", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // --- SUBMISSION SUCCESS DIALOG ---
    if (viewModel.showFormSuccessDialog) {
        Dialog(onDismissRequest = { viewModel.showFormSuccessDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CyberDeepSurface),
                border = BorderStroke(1.5.dp, CyberNeonPurple),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(CyberGlassBg, CircleShape)
                            .border(1.5.dp, CyberAccentPink, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Inquiry Saved",
                            tint = CyberAccentPink,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Text(
                        text = "Registration Saved!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGlowingWhite,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Your counseling request is stored locally. An expert advisor from our Perinthalmanna physical campus will connect with you via Phone or Email shortly to finalize placement details.",
                        fontSize = 13.sp,
                        color = CyberLightGray,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.showFormSuccessDialog = false },
                            border = BorderStroke(1.dp, CyberMutedPurple),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberMutedPurple),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Ok")
                        }
                        Button(
                            onClick = {
                                viewModel.showFormSuccessDialog = false
                                showInquiryDrawer = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberNeonPurple),
                            modifier = Modifier.weight(1.2f)
                        ) {
                            Text("View Status")
                        }
                    }
                }
            }
        }
    }

    // --- HISTORY SIDE SHEET / FULL DIALOG ---
    if (showInquiryDrawer) {
        InquiryHistorySheet(
            inquiries = inquiries,
            onDismiss = { showInquiryDrawer = false },
            onDelete = { id -> viewModel.deleteInquiry(id) }
        )
    }
}

// ==================== UI SECTIONS & COMPONENT BLOCKS ====================

@Composable
fun SectionHeader(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = subtitle.uppercase(),
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = CyberAccentPink,
            letterSpacing = 1.5.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = CyberGlowingWhite,
            lineHeight = 28.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(3.dp)
                .background(CyberNeonPurple, CircleShape)
        )
    }
}

@Composable
fun HeroSection(
    onExploreCoursesIndex: () -> Unit,
    onBookCounselingIndex: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
    ) {
        // Hero Background Artwork loaded from system generated resource (Gracefully handled)
        Image(
            painter = painterResource(id = R.drawable.img_hero_banner),
            contentDescription = "Futuristic Tech Classroom",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay for glass integration and legibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            CyberDarkPurple.copy(alpha = 0.5f),
                            CyberDarkPurple
                        )
                    )
                )
        )

        // Floating ambient particle highlights
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .background(CyberGlassBg.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .border(0.5.dp, CyberNeonPurple, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "PERINTHALMANNA, KERALA",
                    color = CyberAccentPink,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Future-Ready Skills\nfor Real-World Careers",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = CyberGlowingWhite,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "CyberWise Skillversity bridges academic learning with industry-demanded skills. Master AI-powered Digital Marketing, Videography, E-Commerce, and Spoken English.",
                fontSize = 13.sp,
                color = CyberLightGray,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onExploreCoursesIndex,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberNeonPurple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                ) {
                    Text("Explore Courses", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = onBookCounselingIndex,
                    border = BorderStroke(1.5.dp, CyberGlowingWhite),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberGlowingWhite),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1.1f)
                        .height(46.dp)
                ) {
                    Text("Free Counseling", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun StatisticsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        val statsList = listOf(
            Triple("500+", "Students Trained", Icons.Default.Person),
            Triple("50+", "Industry Projects", Icons.Default.PlayArrow),
            Triple("95%", "Satisfaction Rate", Icons.Default.ThumbUp),
            Triple("100+", "Career Placements", Icons.Default.Star)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // First pair
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard(statsList[0])
                StatCard(statsList[2])
            }
            // Second pair
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard(statsList[1])
                StatCard(statsList[3])
            }
        }
    }
}

@Composable
fun StatCard(stat: Triple<String, String, ImageVector>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CyberGlassBg),
        border = BorderStroke(1.dp, CyberGlassBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(CyberNeonPurple.copy(alpha = 0.15f), CircleShape)
                    .border(0.5.dp, CyberNeonPurple, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = stat.third,
                    contentDescription = null,
                    tint = CyberNeonPurple,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column {
                Text(
                    text = stat.first,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = CyberAccentPink
                )
                Text(
                    text = stat.second,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = CyberLightGray
                )
            }
        }
    }
}

@Composable
fun CourseCard(
    course: Course,
    onLearnMore: () -> Unit,
    onQuickRegister: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CyberDeepSurface),
        border = BorderStroke(1.dp, CyberGlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Vector Icons mapping
                val icon = when (course.iconName) {
                    "trending_up" -> Icons.Default.Search
                    "videocam" -> Icons.Default.PlayArrow
                    "shopping_cart" -> Icons.Default.ShoppingCart
                    "record_voice_over" -> Icons.Default.Person
                    else -> Icons.Default.Info
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(CyberNeonPurple.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .border(1.dp, CyberNeonPurple, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = course.title,
                        tint = CyberNeonPurple,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .background(CyberAccentPink.copy(alpha = 0.2f), RoundedCornerShape(50.dp))
                        .border(0.5.dp, CyberAccentPink, RoundedCornerShape(50.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = course.tag,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberAccentPink,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = course.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = CyberGlowingWhite
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = course.shortDesc,
                fontSize = 12.sp,
                color = CyberLightGray,
                lineHeight = 17.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onLearnMore,
                    border = BorderStroke(1.dp, CyberNeonPurple),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberNeonPurple),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                ) {
                    Text("Curriculum Details", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onQuickRegister,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberNeonPurple),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                ) {
                    Text("Select Track", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DegreePathwaysSection() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CyberGlassBg),
        border = BorderStroke(1.2.dp, CyberNeonPurple.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(CyberAccentPink.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "ACCREDITED SECTIONS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CyberAccentPink,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "UGC recognized educational pathways are seamlessly intertwined with our physical skill-building boot camps. Earn official degrees while mastering real commerce tools and digital marketing pipelines directly.",
                fontSize = 13.sp,
                color = CyberGlowingWhite,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            val benefits = listOf(
                "Recognized Degrees for secure government and global migration credentials.",
                "Real Industry Skills needed in modern technology environments.",
                "Excellent Career Advancement opportunities with global digital brands.",
                "Comprehensive Higher Education path with active mentoring sessions."
            )

            benefits.forEach { benefit ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Benefit Indicator",
                        tint = CyberAccentPink,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = benefit,
                        fontSize = 12.sp,
                        color = CyberLightGray,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
fun WhyChooseUsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val features = listOf(
            Pair("Industry-Oriented Curriculum", Icons.Default.PlayArrow),
            Pair("AI Integrated Learning", Icons.Default.Settings),
            Pair("Live Projects", Icons.Default.PlayArrow),
            Pair("Practical Training", Icons.Default.Check),
            Pair("Expert Mentors", Icons.Default.Star),
            Pair("Internship Opportunities", Icons.Default.Star),
            Pair("Career Guidance", Icons.Default.Info),
            Pair("Placement Assistance", Icons.Default.Home),
            Pair("Portfolio Development", Icons.Default.Star),
            Pair("Entrepreneurship Support", Icons.Default.PlayArrow),
            Pair("UGC Accredited Pathways", Icons.Default.Person)
        )

        // Split into chunks of 2 for beautiful adaptive responsive grid
        features.chunked(2).forEach { rowFeatures ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowFeatures.forEach { feature ->
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = CyberDeepSurface.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(0.6.dp, CyberGlassBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = feature.second,
                                contentDescription = feature.first,
                                tint = CyberAccentPink,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = feature.first,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberGlowingWhite,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                // Placeholder filler if odd number
                if (rowFeatures.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun LearningProcessSection() {
    val steps = listOf(
        Pair("Step 1", "Enroll & Career Assessment"),
        Pair("Step 2", "Skill-Based Training"),
        Pair("Step 3", "Live Projects & Practical Experience"),
        Pair("Step 4", "Portfolio Development"),
        Pair("Step 5", "Internship & Placement Support"),
        Pair("Step 6", "Career Success")
    )

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CyberDeepSurface),
        border = BorderStroke(1.dp, CyberGlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            steps.forEachIndexed { index, step ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Node dot graphic with line connection indicator
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(36.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(CyberNeonPurple.copy(alpha = 0.4f), CircleShape)
                                .border(1.5.dp, CyberNeonPurple, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberGlowingWhite
                            )
                        }
                        if (index < steps.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(28.dp)
                                    .background(CyberNeonPurple.copy(alpha = 0.3f))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.padding(bottom = if (index < steps.size - 1) 16.dp else 0.dp)
                    ) {
                        Text(
                            text = step.first.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberAccentPink,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = step.second,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberGlowingWhite
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TestimonialSlider(viewModel: CyberViewModel) {
    val currentTestimonial = viewModel.testimonials[viewModel.selectedTestimonialIndex]

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CyberGlassBg),
        border = BorderStroke(1.dp, CyberGlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Stars indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(currentTestimonial.rating) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        tint = CyberAccentPink,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "\"${currentTestimonial.feedback}\"",
                fontSize = 13.sp,
                color = CyberGlowingWhite,
                lineHeight = 19.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Profile placeholder
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(CyberNeonPurple, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentTestimonial.name.take(1),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = currentTestimonial.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberGlowingWhite
                        )
                        Text(
                            text = currentTestimonial.role,
                            fontSize = 10.sp,
                            color = CyberLightGray
                        )
                    }
                }

                // Nav controller buttons
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = { viewModel.prevTestimonial() },
                        modifier = Modifier
                            .size(30.dp)
                            .background(CyberDeepSurface, CircleShape)
                            .border(0.5.dp, CyberNeonPurple, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Previous Testimonial",
                            tint = CyberGlowingWhite,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.nextTestimonial() },
                        modifier = Modifier
                            .size(30.dp)
                            .background(CyberDeepSurface, CircleShape)
                            .border(0.5.dp, CyberNeonPurple, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Next Testimonial",
                            tint = CyberGlowingWhite,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FaqAccordionList(viewModel: CyberViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        viewModel.faqs.forEach { faq ->
            val isExpanded = viewModel.expandedFaqMap[faq.id] ?: false

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleFaq(faq.id) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isExpanded) CyberDeepSurface else CyberDeepSurface.copy(alpha = 0.5f)
                ),
                border = BorderStroke(1.dp, if (isExpanded) CyberNeonPurple else CyberGlassBorder)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .animateContentSize() // Smooth transition resizing
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = faq.question,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberGlowingWhite,
                            modifier = Modifier.weight(0.9f)
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = CyberNeonPurple,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = faq.answer,
                            fontSize = 12.sp,
                            color = CyberLightGray,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AboutUsSection() {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CyberDeepSurface),
        border = BorderStroke(1.dp, CyberGlassBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_logo),
                    contentDescription = "CyberWise University Logo",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.5.dp, CyberNeonPurple, RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Column {
                    Text(
                        text = "About the Institute",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = CyberAccentPink,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "CyberWise Skillversity",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberGlowingWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "CyberWise Skillversity is a next-generation career-focused learning institution dedicated to bridging the gap between traditional academic education and real-world industry opportunities in Perinthalmanna, Kerala.",
                fontSize = 12.sp,
                color = CyberLightGray,
                lineHeight = 17.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "We empower students, graduates, entrepreneurs, and working professionals with future-ready skills that align with today's rapidly evolving digital economy. Our programs combine practical training, live projects, industry mentorship, and hands-on learning experiences.",
                fontSize = 12.sp,
                color = CyberLightGray,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Mission Box
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = CyberGlassBg),
                    border = BorderStroke(0.6.dp, CyberGlassBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "MISSION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = CyberAccentPink
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "To transform education by integrating academic excellence with industry-relevant skills, enabling learners to build successful global careers.",
                            fontSize = 11.sp,
                            color = CyberLightGray,
                            lineHeight = 15.sp
                        )
                    }
                }

                // Vision Box
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = CyberGlassBg),
                    border = BorderStroke(0.6.dp, CyberGlassBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "VISION",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = CyberNeonPurple
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "To become Kerala's leading skill-based ecosystem where innovation, technology, and practical learning create globally competent professionals.",
                            fontSize = 11.sp,
                            color = CyberLightGray,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContactFormSection(viewModel: CyberViewModel) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CyberDeepSurface),
        border = BorderStroke(1.2.dp, CyberNeonPurple)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Apply Now",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CyberAccentPink,
                letterSpacing = 1.sp
            )
            Text(
                text = "Secure Your Counseling Slot",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CyberGlowingWhite
            )

            // Form inputs
            OutlinedTextField(
                value = viewModel.nameState,
                onValueChange = { viewModel.nameState = it },
                label = { Text("Full Name") },
                placeholder = { Text("Enter your name") },
                colors = textFieldColors(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("name_input"),
                singleLine = true
            )

            OutlinedTextField(
                value = viewModel.phoneState,
                onValueChange = { viewModel.phoneState = it },
                label = { Text("Phone Number") },
                placeholder = { Text("e.g. +91 98765 43210") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = textFieldColors(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("phone_input"),
                singleLine = true
            )

            OutlinedTextField(
                value = viewModel.emailState,
                onValueChange = { viewModel.emailState = it },
                label = { Text("Email Address") },
                placeholder = { Text("e.g. name@domain.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = textFieldColors(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("email_input"),
                singleLine = true
            )

            // Simple Dropdown selection simulated cleanly with card click
            var expandedDropdown by remember { mutableStateOf(false) }
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = viewModel.selectedCourseState,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Course of Interest") },
                    trailingIcon = {
                        IconButton(onClick = { expandedDropdown = !expandedDropdown }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Choose Track")
                        }
                    },
                    colors = textFieldColors(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(CyberDeepSurface)
                        .border(1.dp, CyberNeonPurple, RoundedCornerShape(8.dp))
                ) {
                    viewModel.courses.forEach { course ->
                        DropdownMenuItem(
                            text = { Text(course.title, color = CyberGlowingWhite, fontSize = 13.sp) },
                            onClick = {
                                viewModel.selectedCourseState = course.title
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = viewModel.messageState,
                onValueChange = { viewModel.messageState = it },
                label = { Text("Message (Optional)") },
                placeholder = { Text("Tell us about your learning goals...") },
                colors = textFieldColors(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                maxLines = 3
            )

            viewModel.formErrorMessage?.let { errMsg ->
                Text(
                    text = errMsg,
                    color = CyberAccentPink,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { viewModel.submitInquiry() },
                enabled = !viewModel.isSubmitting,
                colors = ButtonDefaults.buttonColors(containerColor = CyberNeonPurple),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_button")
            ) {
                if (viewModel.isSubmitting) {
                    CircularProgressIndicator(color = CyberGlowingWhite, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Start Your Career Journey Today",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FooterSection(onScrollToSection: (Int) -> Unit) {
    val context = LocalContext.current
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .background(Color(0xFF0A011A))
            .border(BorderStroke(1.dp, Brush.verticalGradient(listOf(CyberGlassBorder, Color.Transparent))))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "CyberWise Skillversity",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberGlowingWhite
                )
                Text(
                    text = "Where Education Meets Industry and Careers Begin.",
                    fontSize = 11.sp,
                    color = CyberLightGray
                )
            }

            Divider(color = CyberGlassBorder, thickness = 0.5.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Navigation Quicklinks
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Quicklinks", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberAccentPink)
                    Text("Hero Welcome", fontSize = 10.sp, color = CyberLightGray, modifier = Modifier.clickable { onScrollToSection(0) })
                    Text("Our Courses", fontSize = 10.sp, color = CyberLightGray, modifier = Modifier.clickable { onScrollToSection(2) })
                    Text("Degree Pathways", fontSize = 10.sp, color = CyberLightGray, modifier = Modifier.clickable { onScrollToSection(7) })
                    Text("Counseling Desk", fontSize = 10.sp, color = CyberLightGray, modifier = Modifier.clickable { onScrollToSection(10) })
                }

                // Socials simulated links
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Contact details", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyberNeonPurple)
                    Text("Perinthalmanna, Kerala", fontSize = 10.sp, color = CyberLightGray)
                    Text("Call: +91 90000 00000", fontSize = 10.sp, color = CyberLightGray)
                    Text("Email: info@cyberwise.edu", fontSize = 10.sp, color = CyberLightGray)
                }
            }

            Divider(color = CyberGlassBorder, thickness = 0.5.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "© 2026 CyberWise Skillversity. All Rights Reserved.",
                    fontSize = 9.sp,
                    color = CyberMutedPurple
                )

                // Privacy and Terms Policy Links
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Privacy Policy",
                        fontSize = 9.sp,
                        color = CyberMutedPurple,
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "CyberWise Privacy Policy v1.0", Toast.LENGTH_SHORT).show()
                        }
                    )
                    Text(
                        text = "Terms & Conditions",
                        fontSize = 9.sp,
                        color = CyberMutedPurple,
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "CyberWise Terms of Service v1.0", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

// Custom text field coloring configuration
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = CyberGlowingWhite,
    unfocusedTextColor = CyberGlowingWhite,
    focusedLabelColor = CyberNeonPurple,
    unfocusedLabelColor = CyberMutedPurple,
    focusedBorderColor = CyberNeonPurple,
    unfocusedBorderColor = CyberGlassBorder,
    focusedContainerColor = CyberGlassBg,
    unfocusedContainerColor = CyberDarkPurple.copy(alpha = 0.5f)
)

// ==================== LOWER MODULE DIALOGS & DRAWERS ====================

@Composable
fun CourseDetailsDialog(
    course: Course,
    onDismiss: () -> Unit,
    onEnroll: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CyberDeepSurface),
            border = BorderStroke(1.5.dp, CyberNeonPurple),
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(CyberAccentPink.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = course.tag.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberAccentPink,
                            letterSpacing = 0.5.sp
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = CyberGlowingWhite)
                    }
                }

                Text(
                    text = course.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberGlowingWhite
                )

                Text(
                    text = course.longDesc,
                    fontSize = 12.sp,
                    color = CyberLightGray,
                    lineHeight = 17.sp
                )

                Divider(color = CyberGlassBorder, thickness = 0.5.dp)

                Text(
                    text = "Core Curriculum & Learning Modules",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyberNeonPurple,
                    letterSpacing = 0.5.sp
                )

                course.topics.forEach { topic ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .size(6.dp)
                                .background(CyberAccentPink, CircleShape)
                        )
                        Text(
                            text = topic,
                            fontSize = 11.sp,
                            color = CyberGlowingWhite,
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        border = BorderStroke(1.dp, CyberLightGray),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberLightGray),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Back", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onEnroll,
                        colors = ButtonDefaults.buttonColors(containerColor = CyberNeonPurple),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Text("Register for Track", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun InquiryHistorySheet(
    inquiries: List<Inquiry>,
    onDismiss: () -> Unit,
    onDelete: (Int) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CyberDeepSurface),
            border = BorderStroke(1.5.dp, CyberAccentPink),
            modifier = Modifier
                .padding(vertical = 24.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = CyberAccentPink)
                        Text(
                            text = "My Counseling Requests",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyberGlowingWhite
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = CyberGlowingWhite)
                    }
                }

                Text(
                    text = "These registrations are securely stored in your local offline app database. An advisor will contact you with counselor materials shortly.",
                    fontSize = 11.sp,
                    color = CyberLightGray,
                    lineHeight = 15.sp
                )

                Divider(color = CyberGlassBorder, thickness = 0.5.dp)

                if (inquiries.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "",
                                tint = CyberMutedPurple,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "No inquiries created yet.",
                                fontSize = 13.sp,
                                color = CyberLightGray
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(inquiries) { inquiry ->
                            InquiryItemRow(inquiry = inquiry, onDelete = { onDelete(inquiry.id) })
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = CyberNeonPurple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close Panel", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun InquiryItemRow(inquiry: Inquiry, onDelete: () -> Unit) {
    val formatter = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }
    val formattedDate = remember(inquiry.timestamp) { formatter.format(Date(inquiry.timestamp)) }

    Card(
        colors = CardDefaults.cardColors(containerColor = CyberGlassBg),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(0.6.dp, CyberGlassBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(0.9f)) {
                    Text(
                        text = inquiry.courseInterested,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberAccentPink
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "For Student: ${inquiry.name}",
                        fontSize = 12.sp,
                        color = CyberGlowingWhite
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Cancel request",
                        tint = Color.Red.copy(alpha = 0.8f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (inquiry.message.isNotBlank()) {
                Text(
                    text = "\"${inquiry.message}\"",
                    fontSize = 11.sp,
                    color = CyberLightGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    fontSize = 9.sp,
                    color = CyberMutedPurple
                )

                Box(
                    modifier = Modifier
                        .background(CyberNeonPurple.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Pending Advisor Call",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberNeonPurple
                    )
                }
            }
        }
    }
}
