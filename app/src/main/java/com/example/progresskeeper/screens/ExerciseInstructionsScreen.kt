package com.example.progresskeeper.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import android.os.Build

@Composable
fun ExerciseInstructionsScreen(
    exercise: String,
    onHomeClick: () -> Unit
) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }
    
    var showError by remember { mutableStateOf(false) }
    
    val instructions = when (exercise) {
        "Barbell Shrugs" -> listOf(
            "Stand with your feet shoulder-width apart",
            "Hold a barbell in front of your thighs with an overhand grip",
            "Keep your arms straight and shoulders relaxed",
            "Lift your shoulders straight up toward your ears",
            "Hold the contraction for a second",
            "Lower the weight back down slowly"
        )
        "Dumbbell Shrugs" -> listOf(
            "Stand with your feet shoulder-width apart",
            "Hold a dumbbell in each hand at your sides",
            "Keep your arms straight and shoulders relaxed",
            "Lift your shoulders straight up toward your ears",
            "Hold the contraction for a second",
            "Lower the weights back down slowly"
        )
        "Behind-the-Back Smith Machine Shrugs" -> listOf(
            "Stand with your back to the Smith machine",
            "Grab the bar behind your back with an overhand grip",
            "Keep your arms straight and shoulders relaxed",
            "Lift your shoulders straight up toward your ears",
            "Hold the contraction for a second",
            "Lower the weight back down slowly"
        )
        "Rack Pulls" -> listOf(
            "Set up a barbell in a power rack at mid-thigh height",
            "Stand close to the bar with your feet shoulder-width apart",
            "Bend at your hips and knees to grab the bar",
            "Keep your back straight and chest up",
            "Pull the bar up by extending your hips and knees",
            "Squeeze your shoulder blades together at the top",
            "Lower the bar back down slowly"
        )
        "Face Pulls" -> listOf(
            "Attach a rope handle to a high pulley",
            "Stand facing the machine with your feet shoulder-width apart",
            "Grab the rope with both hands, palms facing each other",
            "Pull the rope toward your face, separating your hands as you pull",
            "Keep your elbows high and out to the sides",
            "Squeeze your shoulder blades together at the end of the movement",
            "Slowly return to the starting position"
        )
        "Military Press" -> listOf(
            "Stand with your feet shoulder-width apart",
            "Hold a barbell at shoulder height with an overhand grip",
            "Keep your core tight and back straight",
            "Press the bar overhead until your arms are fully extended",
            "Lower the bar back to shoulder height with control",
            "Keep your elbows slightly in front of the bar"
        )
        "Lateral Raises" -> listOf(
            "Stand with your feet shoulder-width apart",
            "Hold dumbbells at your sides with palms facing inward",
            "Keep a slight bend in your elbows",
            "Raise your arms out to the sides until they're parallel to the floor",
            "Lower the weights back down with control",
            "Keep your core engaged throughout the movement"
        )
        "Front Raises" -> listOf(
            "Stand with your feet shoulder-width apart",
            "Hold a dumbbell in each hand in front of your thighs",
            "Keep your arms straight with a slight bend in the elbows",
            "Raise your arms forward until they're parallel to the floor",
            "Lower the weights back down with control",
            "Keep your core engaged throughout the movement"
        )
        "Reverse Flyes" -> listOf(
            "Stand with your feet shoulder-width apart",
            "Bend at your hips with a slight bend in your knees",
            "Hold dumbbells with palms facing each other",
            "Raise your arms out to the sides, squeezing your shoulder blades together",
            "Lower the weights back down with control",
            "Keep your back straight throughout the movement"
        )
        "Arnold Press" -> listOf(
            "Sit on a bench with back support",
            "Hold dumbbells at shoulder height with palms facing you",
            "Press the weights overhead while rotating your palms outward",
            "At the top, your palms should face forward",
            "Lower the weights while rotating your palms back to the starting position",
            "Keep your core engaged throughout the movement"
        )
        "Bench Press" -> listOf(
            "Lie on a flat bench with your feet flat on the floor",
            "Grip the bar slightly wider than shoulder-width",
            "Unrack the bar and lower it to your mid-chest",
            "Keep your elbows at about 45 degrees from your body",
            "Press the bar back up until your arms are fully extended",
            "Keep your core tight and maintain a slight arch in your lower back"
        )
        "Incline Dumbbell Press" -> listOf(
            "Set the bench to a 30-45 degree angle",
            "Hold dumbbells at shoulder height with palms facing forward",
            "Press the weights up until your arms are fully extended",
            "Lower the weights back to shoulder height with control",
            "Keep your core engaged and maintain proper shoulder position",
            "Don't let your elbows flare out too much"
        )
        "Dips" -> listOf(
            "Grip the parallel bars with your hands slightly wider than shoulder-width",
            "Support your body with straight arms",
            "Lower your body by bending your elbows",
            "Keep your elbows close to your body",
            "Lower until your upper arms are parallel to the floor",
            "Press back up to the starting position"
        )
        "Cable Flyes" -> listOf(
            "Stand between two cable machines",
            "Grab the handles with your arms extended to the sides",
            "Take a step forward to create tension",
            "Bring your arms forward in an arc motion",
            "Squeeze your chest at the end of the movement",
            "Return to the starting position with control"
        )
        "Push-Ups" -> listOf(
            "Start in a plank position with hands slightly wider than shoulder-width",
            "Keep your body in a straight line from head to heels",
            "Lower your body by bending your elbows",
            "Keep your elbows at about 45 degrees from your body",
            "Lower until your chest nearly touches the floor",
            "Press back up to the starting position"
        )
        "Pull-Ups" -> listOf(
            "Grab the pull-up bar with hands slightly wider than shoulder-width",
            "Hang with arms fully extended",
            "Pull your body up until your chin is over the bar",
            "Keep your core engaged throughout the movement",
            "Lower yourself back down with control",
            "Maintain proper shoulder position"
        )
        "Barbell Rows" -> listOf(
            "Stand with feet shoulder-width apart",
            "Bend at your hips and knees to grab the bar",
            "Keep your back straight and chest up",
            "Pull the bar to your lower chest/upper abdomen",
            "Squeeze your shoulder blades together at the top",
            "Lower the bar back down with control"
        )
        "Lat Pulldowns" -> listOf(
            "Sit at the lat pulldown machine",
            "Grab the bar with a wide overhand grip",
            "Pull the bar down to your upper chest",
            "Keep your chest up and core engaged",
            "Squeeze your shoulder blades together at the bottom",
            "Return to the starting position with control"
        )
        "Deadlifts" -> listOf(
            "Stand with feet shoulder-width apart",
            "Bend at your hips and knees to grab the bar",
            "Keep your back straight and chest up",
            "Lift the bar by extending your hips and knees",
            "Stand up straight with shoulders back",
            "Lower the bar back down with control"
        )
        "T-Bar Rows" -> listOf(
            "Stand over the T-bar with feet shoulder-width apart",
            "Bend at your hips and knees to grab the handles",
            "Keep your back straight and chest up",
            "Pull the weight to your chest",
            "Squeeze your shoulder blades together at the top",
            "Lower the weight back down with control"
        )
        "Tricep Pushdowns" -> listOf(
            "Stand facing a high pulley machine",
            "Grab the bar with an overhand grip, hands shoulder-width apart",
            "Keep your elbows close to your body",
            "Push the bar down until your arms are fully extended",
            "Squeeze your triceps at the bottom",
            "Return to the starting position with control"
        )
        "Skull Crushers" -> listOf(
            "Lie on a flat bench",
            "Hold a barbell or EZ bar above your chest",
            "Lower the weight toward your forehead by bending your elbows",
            "Keep your upper arms perpendicular to the floor",
            "Extend your arms back to the starting position",
            "Keep your elbows in a fixed position"
        )
        "Close-Grip Bench Press" -> listOf(
            "Lie on a flat bench",
            "Grip the bar with hands closer than shoulder-width",
            "Unrack the bar and lower it to your mid-chest",
            "Keep your elbows close to your body",
            "Press the bar back up until your arms are fully extended",
            "Keep your core tight throughout the movement"
        )
        "Overhead Tricep Extensions" -> listOf(
            "Stand with feet shoulder-width apart",
            "Hold a dumbbell with both hands behind your head",
            "Keep your upper arms close to your ears",
            "Extend your arms upward",
            "Lower the weight back down behind your head",
            "Keep your elbows pointing forward"
        )
        "Diamond Push-Ups" -> listOf(
            "Start in a push-up position",
            "Place your hands close together, forming a diamond shape",
            "Keep your body in a straight line",
            "Lower your body by bending your elbows",
            "Keep your elbows close to your body",
            "Press back up to the starting position"
        )
        "Barbell Curls" -> listOf(
            "Stand with feet shoulder-width apart",
            "Hold a barbell with an underhand grip",
            "Keep your elbows close to your body",
            "Curl the bar up toward your shoulders",
            "Squeeze your biceps at the top",
            "Lower the bar back down with control"
        )
        "Hammer Curls" -> listOf(
            "Stand with feet shoulder-width apart",
            "Hold dumbbells with palms facing inward",
            "Keep your elbows close to your body",
            "Curl the weights up toward your shoulders",
            "Squeeze your biceps at the top",
            "Lower the weights back down with control"
        )
        "Preacher Curls" -> listOf(
            "Sit at a preacher curl bench",
            "Rest your arms on the pad",
            "Hold a barbell or dumbbells with an underhand grip",
            "Curl the weight up toward your shoulders",
            "Squeeze your biceps at the top",
            "Lower the weight back down with control"
        )
        "Incline Dumbbell Curls" -> listOf(
            "Set the bench to a 45-degree angle",
            "Sit back with dumbbells at your sides",
            "Keep your elbows close to your body",
            "Curl the weights up toward your shoulders",
            "Squeeze your biceps at the top",
            "Lower the weights back down with control"
        )
        "Concentration Curls" -> listOf(
            "Sit on a bench with legs spread",
            "Rest your elbow on your inner thigh",
            "Hold a dumbbell with an underhand grip",
            "Curl the weight up toward your shoulder",
            "Squeeze your bicep at the top",
            "Lower the weight back down with control"
        )
        "Wrist Curls" -> listOf(
            "Sit on a bench with forearms resting on thighs",
            "Hold a barbell or dumbbells with an underhand grip",
            "Let your wrists hang over your knees",
            "Curl your wrists up toward your body",
            "Squeeze your forearms at the top",
            "Lower the weight back down with control"
        )
        "Reverse Wrist Curls" -> listOf(
            "Sit on a bench with forearms resting on thighs",
            "Hold a barbell or dumbbells with an overhand grip",
            "Let your wrists hang over your knees",
            "Curl your wrists up toward your body",
            "Squeeze your forearms at the top",
            "Lower the weight back down with control"
        )
        "Farmers Walks" -> listOf(
            "Stand with feet shoulder-width apart",
            "Hold heavy dumbbells at your sides",
            "Keep your shoulders back and chest up",
            "Walk forward with short, controlled steps",
            "Maintain proper posture throughout",
            "Keep your core engaged"
        )
        "Plate Pinches" -> listOf(
            "Stand with feet shoulder-width apart",
            "Hold two weight plates together",
            "Pinch the plates between your thumb and fingers",
            "Hold for the desired duration",
            "Keep your wrist straight",
            "Maintain proper posture"
        )
        "Behind-the-Back Wrist Curls" -> listOf(
            "Stand with feet shoulder-width apart",
            "Hold a barbell behind your back",
            "Keep your arms straight",
            "Curl your wrists up toward your body",
            "Squeeze your forearms at the top",
            "Lower the weight back down with control"
        )
        "Squats" -> listOf(
            "Stand with feet shoulder-width apart",
            "Keep your chest up and core engaged",
            "Bend at your hips and knees",
            "Lower until thighs are parallel to the floor",
            "Keep your knees in line with your toes",
            "Drive through your heels to stand back up"
        )
        "Romanian Deadlifts" -> listOf(
            "Stand with feet shoulder-width apart",
            "Hold a barbell with an overhand grip",
            "Keep your back straight and chest up",
            "Hinge at your hips, pushing them back",
            "Lower the bar along your legs",
            "Return to standing by driving your hips forward"
        )
        "Leg Press" -> listOf(
            "Sit on the leg press machine",
            "Place your feet shoulder-width apart on the platform",
            "Keep your back flat against the pad",
            "Release the safety handles",
            "Lower the platform by bending your knees",
            "Press the platform back up to starting position"
        )
        "Lunges" -> listOf(
            "Stand with feet together",
            "Take a step forward with one leg",
            "Lower your body until both knees are at 90 degrees",
            "Keep your front knee above your ankle",
            "Push through your front heel to return to standing",
            "Alternate legs for each rep"
        )
        "Leg Extensions" -> listOf(
            "Sit on the leg extension machine",
            "Adjust the pad to rest on your lower shins",
            "Keep your back straight against the pad",
            "Extend your legs until they're straight",
            "Squeeze your quadriceps at the top",
            "Lower the weight back down with control"
        )
        "Standing Calf Raises" -> listOf(
            "Stand on a raised platform or step",
            "Place the balls of your feet on the edge",
            "Hold onto something for balance",
            "Lower your heels below the platform",
            "Raise up onto your toes as high as possible",
            "Lower back down with control"
        )
        "Seated Calf Raises" -> listOf(
            "Sit on the calf raise machine",
            "Place the balls of your feet on the platform",
            "Adjust the pad to rest on your thighs",
            "Lower your heels as far as possible",
            "Raise up onto your toes as high as possible",
            "Lower back down with control"
        )
        "Donkey Calf Raises" -> listOf(
            "Stand on a raised platform",
            "Bend at your hips, placing hands on a support",
            "Have a partner sit on your lower back",
            "Lower your heels below the platform",
            "Raise up onto your toes as high as possible",
            "Lower back down with control"
        )
        "Jump Rope" -> listOf(
            "Hold the rope handles at your sides",
            "Keep your elbows close to your body",
            "Jump on the balls of your feet",
            "Keep your jumps small and controlled",
            "Maintain a steady rhythm",
            "Land softly on each jump"
        )
        "Single-Leg Calf Raises" -> listOf(
            "Stand on one foot on a raised platform",
            "Hold onto something for balance",
            "Lower your heel below the platform",
            "Raise up onto your toe as high as possible",
            "Lower back down with control",
            "Complete all reps on one leg before switching"
        )
        else -> listOf("Instructions coming soon...")
    }
    
    val commonMistakes = when (exercise) {
        "Barbell Shrugs" -> listOf(
            "Using momentum to lift the weight",
            "Rolling the shoulders instead of lifting straight up",
            "Bending the elbows during the movement",
            "Not maintaining proper posture"
        )
        "Dumbbell Shrugs" -> listOf(
            "Using momentum to lift the weights",
            "Rolling the shoulders instead of lifting straight up",
            "Bending the elbows during the movement",
            "Not maintaining proper posture"
        )
        "Behind-the-Back Smith Machine Shrugs" -> listOf(
            "Using momentum to lift the weight",
            "Rolling the shoulders instead of lifting straight up",
            "Bending the elbows during the movement",
            "Not maintaining proper posture"
        )
        "Rack Pulls" -> listOf(
            "Rounding the back during the lift",
            "Using too much weight and compromising form",
            "Not engaging the core properly",
            "Not fully extending at the top of the movement"
        )
        "Face Pulls" -> listOf(
            "Pulling the rope too low",
            "Not separating the hands enough at the end of the movement",
            "Using momentum to pull the weight",
            "Not maintaining proper posture"
        )
        "Military Press" -> listOf(
            "Arching the back excessively",
            "Not fully extending the arms at the top",
            "Using momentum to lift the weight",
            "Not maintaining proper shoulder position"
        )
        "Lateral Raises" -> listOf(
            "Using momentum to lift the weights",
            "Raising the arms too high",
            "Not maintaining proper shoulder position",
            "Not controlling the negative portion"
        )
        "Front Raises" -> listOf(
            "Using momentum to lift the weights",
            "Raising the arms too high",
            "Not maintaining proper shoulder position",
            "Not controlling the negative portion"
        )
        "Reverse Flyes" -> listOf(
            "Using momentum to lift the weights",
            "Not maintaining proper back position",
            "Raising the arms too high",
            "Not controlling the negative portion"
        )
        "Arnold Press" -> listOf(
            "Not completing the full rotation",
            "Using momentum to lift the weights",
            "Not maintaining proper shoulder position",
            "Arching the back excessively"
        )
        "Bench Press" -> listOf(
            "Bouncing the bar off the chest",
            "Not maintaining proper shoulder position",
            "Flaring the elbows too much",
            "Not controlling the negative portion"
        )
        "Incline Dumbbell Press" -> listOf(
            "Not maintaining proper shoulder position",
            "Using momentum to lift the weights",
            "Not controlling the negative portion",
            "Setting the bench angle too high"
        )
        "Dips" -> listOf(
            "Not going deep enough",
            "Flaring the elbows too much",
            "Not maintaining proper shoulder position",
            "Using momentum to complete the movement"
        )
        "Cable Flyes" -> listOf(
            "Using momentum to complete the movement",
            "Not maintaining proper shoulder position",
            "Not controlling the negative portion",
            "Not taking enough steps forward"
        )
        "Push-Ups" -> listOf(
            "Not maintaining a straight body line",
            "Not going deep enough",
            "Flaring the elbows too much",
            "Not controlling the negative portion"
        )
        "Pull-Ups" -> listOf(
            "Not going through full range of motion",
            "Using momentum to complete the movement",
            "Not maintaining proper shoulder position",
            "Not controlling the negative portion"
        )
        "Barbell Rows" -> listOf(
            "Rounding the back during the movement",
            "Using momentum to lift the weight",
            "Not maintaining proper shoulder position",
            "Not controlling the negative portion"
        )
        "Lat Pulldowns" -> listOf(
            "Pulling the bar behind the neck",
            "Using momentum to complete the movement",
            "Not maintaining proper shoulder position",
            "Not controlling the negative portion"
        )
        "Deadlifts" -> listOf(
            "Rounding the back during the lift",
            "Using too much weight and compromising form",
            "Not maintaining proper hip position",
            "Not controlling the negative portion"
        )
        "T-Bar Rows" -> listOf(
            "Rounding the back during the movement",
            "Using momentum to lift the weight",
            "Not maintaining proper shoulder position",
            "Not controlling the negative portion"
        )
        "Tricep Pushdowns" -> listOf(
            "Moving the elbows away from the body",
            "Using momentum to complete the movement",
            "Not fully extending the arms",
            "Not controlling the negative portion"
        )
        "Skull Crushers" -> listOf(
            "Moving the elbows too much",
            "Not maintaining proper shoulder position",
            "Using too much weight",
            "Not controlling the negative portion"
        )
        "Close-Grip Bench Press" -> listOf(
            "Flaring the elbows too much",
            "Not maintaining proper shoulder position",
            "Using too much weight",
            "Not controlling the negative portion"
        )
        "Overhead Tricep Extensions" -> listOf(
            "Moving the elbows too much",
            "Not maintaining proper shoulder position",
            "Using momentum to complete the movement",
            "Not controlling the negative portion"
        )
        "Diamond Push-Ups" -> listOf(
            "Not maintaining a straight body line",
            "Not going deep enough",
            "Moving the elbows too much",
            "Not controlling the negative portion"
        )
        "Barbell Curls" -> listOf(
            "Swinging the body to lift the weight",
            "Moving the elbows away from the body",
            "Not maintaining proper shoulder position",
            "Not controlling the negative portion"
        )
        "Hammer Curls" -> listOf(
            "Swinging the body to lift the weights",
            "Moving the elbows away from the body",
            "Not maintaining proper shoulder position",
            "Not controlling the negative portion"
        )
        "Preacher Curls" -> listOf(
            "Not maintaining proper arm position on the pad",
            "Using momentum to lift the weight",
            "Not maintaining proper shoulder position",
            "Not controlling the negative portion"
        )
        "Incline Dumbbell Curls" -> listOf(
            "Swinging the body to lift the weights",
            "Moving the elbows away from the body",
            "Not maintaining proper shoulder position",
            "Not controlling the negative portion"
        )
        "Concentration Curls" -> listOf(
            "Moving the elbow off the thigh",
            "Swinging the body to lift the weight",
            "Not maintaining proper shoulder position",
            "Not controlling the negative portion"
        )
        "Wrist Curls" -> listOf(
            "Moving the forearms during the movement",
            "Using too much weight",
            "Not maintaining proper wrist position",
            "Not controlling the negative portion"
        )
        "Reverse Wrist Curls" -> listOf(
            "Moving the forearms during the movement",
            "Using too much weight",
            "Not maintaining proper wrist position",
            "Not controlling the negative portion"
        )
        "Farmers Walks" -> listOf(
            "Rounding the shoulders",
            "Not maintaining proper posture",
            "Taking steps that are too long",
            "Not engaging the core"
        )
        "Plate Pinches" -> listOf(
            "Not maintaining proper grip",
            "Using plates that are too heavy",
            "Not maintaining proper wrist position",
            "Not maintaining proper posture"
        )
        "Behind-the-Back Wrist Curls" -> listOf(
            "Moving the arms during the movement",
            "Using too much weight",
            "Not maintaining proper wrist position",
            "Not controlling the negative portion"
        )
        "Squats" -> listOf(
            "Knees caving inward",
            "Rising hips too fast",
            "Not maintaining proper back position",
            "Not going deep enough"
        )
        "Romanian Deadlifts" -> listOf(
            "Rounding the back",
            "Bending the knees too much",
            "Not maintaining proper hip position",
            "Using too much weight"
        )
        "Leg Press" -> listOf(
            "Locking the knees at the top",
            "Not maintaining proper back position",
            "Using too much weight",
            "Not going through full range of motion"
        )
        "Lunges" -> listOf(
            "Stepping too far forward",
            "Not maintaining proper knee alignment",
            "Leaning too far forward",
            "Not going deep enough"
        )
        "Leg Extensions" -> listOf(
            "Using momentum to complete the movement",
            "Not maintaining proper back position",
            "Using too much weight",
            "Not controlling the negative portion"
        )
        "Standing Calf Raises" -> listOf(
            "Not going through full range of motion",
            "Using momentum to complete the movement",
            "Not maintaining proper balance",
            "Not controlling the negative portion"
        )
        "Seated Calf Raises" -> listOf(
            "Not going through full range of motion",
            "Using momentum to complete the movement",
            "Not maintaining proper back position",
            "Not controlling the negative portion"
        )
        "Donkey Calf Raises" -> listOf(
            "Not going through full range of motion",
            "Using momentum to complete the movement",
            "Not maintaining proper back position",
            "Not controlling the negative portion"
        )
        "Jump Rope" -> listOf(
            "Jumping too high",
            "Not maintaining proper rhythm",
            "Landing too hard",
            "Not keeping the rope at the right length"
        )
        "Single-Leg Calf Raises" -> listOf(
            "Not going through full range of motion",
            "Using momentum to complete the movement",
            "Not maintaining proper balance",
            "Not controlling the negative portion"
        )
        else -> listOf("Common mistakes coming soon...")
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader(
            title = exercise,
            onCalendarClick = {},
            onAddClick = {},
            onHelpClick = {},
            onHomeClick = onHomeClick
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                val gifFileName = when (exercise) {
                    "Barbell Shrugs" -> "barbell_shrug.gif"
                    "Dumbbell Shrugs" -> "dumbbell_shrug.gif"
                    "Behind-the-Back Smith Machine Shrugs" -> "smith-back_shrug.gif"
                    "Rack Pulls" -> "rack_pulls.gif"
                    "Face Pulls" -> "face_pull.gif"
                    "Military Press" -> "military_press.gif"
                    "Lateral Raises" -> "lat_raises.gif"
                    "Front Raises" -> "front_raises.gif"
                    "Reverse Flyes" -> "reverse_flys.gif"
                    "Arnold Press" -> "dumbbell-arnold_press.gif"
                    "Bench Press" -> "barbell_benchpress.gif"
                    "Incline Dumbbell Press" -> "dumbbell incline press.gif"
                    "Dips" -> "dips.gif"
                    "Cable Flyes" -> "cable_flys.gif"
                    "Push-Ups" -> "push_ups.gif"
                    "Pull-Ups" -> "pullups.gif"
                    "Barbell Rows" -> "barbell rows.gif"
                    "Lat Pulldowns" -> "lat pulldowns.gif"
                    "Deadlifts" -> "deadlift.gif"
                    "T-Bar Rows" -> "tbar rows.gif"
                    "Tricep Pushdowns" -> "tricep_pushdowns.gif"
                    "Skull Crushers" -> "skullcrushers.gif"
                    "Close-Grip Bench Press" -> "closs grip bench press.gif"
                    "Overhead Tricep Extensions" -> "overhead_tricep extensions.gif"
                    "Diamond Push-Ups" -> "diamond_push ups.gif"
                    "Barbell Curls" -> "barbell curls.gif"
                    "Hammer Curls" -> "hammer curls.gif"
                    "Preacher Curls" -> "preacher curls.gif"
                    "Incline Dumbbell Curls" -> "incline dumbbell curls.gif"
                    "Concentration Curls" -> "Concentration-Curl.gif"
                    "Wrist Curls" -> "wrist curls.gif"
                    "Reverse Wrist Curls" -> "Reverse-Wrist-Curl.gif"
                    "Farmers Walks" -> "farmer walkers.gif"
                    "Plate Pinches" -> "plate pinches.gif"
                    "Behind-the-Back Wrist Curls" -> "behind the back wrist curls.gif"
                    "Squats" -> "squat.gif"
                    "Romanian Deadlifts" -> "Romanian-deadlift.gif"
                    "Leg Press" -> "leg-press.gif"
                    "Lunges" -> "lunges.gif"
                    "Leg Extensions" -> "leg extensions.gif"
                    "Standing Calf Raises" -> "standing calf raises.gif"
                    "Seated Calf Raises" -> "seated calf raises.gif"
                    "Donkey Calf Raises" -> "donkey calf raises.gif"
                    "Jump Rope" -> "jump rope.gif"
                    "Single-Leg Calf Raises" -> "single leg calf raises.gif"
                    else -> null
                }
                
                if (gifFileName != null) {
                    Log.d("ExerciseInstructions", "Loading GIF: $gifFileName")
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data("file:///android_asset/gifs/$gifFileName")
                                .crossfade(true)
                                .build(),
                            imageLoader = imageLoader,

                        ),
                        contentDescription = "Exercise demonstration",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Instructions",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            
            items(instructions.size) { index ->
                Text(
                    text = "${index + 1}. ${instructions[index]}",
                    fontSize = 16.sp
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Common Mistakes",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            
            items(commonMistakes.size) { index ->
                Text(
                    text = "${index + 1}. ${commonMistakes[index]}",
                    fontSize = 16.sp
                )
            }
        }
    }
} 