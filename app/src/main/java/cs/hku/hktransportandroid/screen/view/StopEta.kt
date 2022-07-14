package cs.hku.hktransportandroid.screen.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cs.hku.hktransportandroid.Entity.MultiLanguage
import cs.hku.hktransportandroid.Entity.StopEtaGrouped
import cs.hku.hktransportandroid.repository.StopEta
import cs.hku.hktransportandroid.util.minutesFromNow
import cs.hku.hktransportandroid.util.toTime
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StopEta(stopEta: StopEtaGrouped, onLongClick:(()->Unit)? = null,iconTint: Color) {
    val toggle = remember {
        mutableStateOf(false)
    }
    val canToggle = stopEta.arrivalTime.size > 1
    Column(modifier =
    Modifier.combinedClickable(
        enabled = canToggle || onLongClick !=null,
        onClick = {
            if (canToggle)
                toggle.value = !toggle.value
        },
        onLongClick = {onLongClick?.invoke()}
    )) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.DirectionsBus,"", modifier = Modifier.size(16.dp), tint = iconTint)
                    Text(text = stopEta.route, fontSize = 20.sp)
                }
                Text(text = stopEta.destination.getWithLocale(LocalContext.current.resources.configuration.locales))
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.padding(end = if (!canToggle) 16.dp else 0.dp),
                fontSize = 30.sp,
                text = stopEta.arrivalTime.firstOrNull()?.minutesFromNow()?.toString()?:"-"
            )
            if (canToggle)
                Icon(
                    modifier = Modifier
                        .width(16.dp)
                        .height(16.dp),
                    imageVector = if (toggle.value) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = ""
                )
        }
        if (toggle.value) {
            stopEta.arrivalTime.drop(1).map {
                Box(
                    contentAlignment = Alignment.CenterEnd,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 32.dp)
                ) {
                    Text(
                        fontSize = 30.sp,
                        text = it.minutesFromNow().toString()

                    )
                }
            }
        }
    }
}

class StopEtaPreviewParameterProvider : PreviewParameterProvider<StopEta> {
    override val values: Sequence<StopEta>
        get() = sequenceOf(
            StopEta(
                co = "",
                dataTimestamp = "",
                destEn = "",
                destSc = "",
                destTc = "彩雲",
                dir = "",
                eta = "2022-05-30T16:50:00+08:00",
                etaSeq = 0,
                rmkEn = "",
                rmkSc = "",
                rmkTc = "",
                route = "3M",
                seq = 0,
                serviceType = 0
            )
        )
}

//class StopEtaGroupedPreviewParameterProvider : PreviewParameterProvider<StopEtaGrouped> {
//    override val values: Sequence<StopEtaGrouped>
//        get() = sequenceOf(
//            StopEtaGrouped(
//                route = "3M",
//                destination = MultiLanguage(
//                    en = "彩雲",
//                    tc = "彩雲",
//                    sc = "彩雲"
//                ), arrivalTime = listOf(
//                    LocalDateTime.now(),
//                    LocalDateTime.now(),
//                    LocalDateTime.now()
//                )
//            )
//        )
//
//}