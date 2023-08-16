package com.walletconnect.web3.modal.ui.components.internal.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.walletconnect.web3.modal.R
import com.walletconnect.web3.modal.ui.previews.MultipleComponentsPreview
import com.walletconnect.web3.modal.ui.previews.UiModePreview
import com.walletconnect.web3.modal.ui.theme.Web3ModalTheme

@Composable
internal fun ActionEntry(
    text: String,
    modifier: Modifier = Modifier,
    icon: @Composable() ((Color) -> Unit)? = null,
    isEnabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClick: () -> Unit
) {
    val background: Color
    val textColor: Color
    if (isEnabled) {
        textColor = Web3ModalTheme.colors.foreground.color200
        background = Web3ModalTheme.colors.overlay02
    } else {
        textColor = Web3ModalTheme.colors.foreground.color300
        background = Web3ModalTheme.colors.overlay10
    }
    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(contentPadding)
    ) {
        Row(
            modifier = modifier
                .clickable { onClick() }
                .background(background)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                it(textColor)
                HorizontalSpacer(width = 8.dp)
            }
            Text(text = text, style = Web3ModalTheme.typo.paragraph600.copy(color = textColor))
        }
    }
}

@Composable
internal fun CopyActionEntry(modifier: Modifier = Modifier, isEnabled: Boolean = true, onClick: () -> Unit) {
    ActionEntry(
        text = "Copy link",
        modifier = Modifier.height(56.dp).then(modifier),
        onClick = onClick,
        isEnabled = isEnabled,
        icon = {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_copy),
                contentDescription = ContentDescription.COPY.description,
                colorFilter = ColorFilter.tint(it),
                modifier = Modifier.size(14.dp)
            )
        }
    )
}

@UiModePreview
@Composable
private fun ActionEntryPreview() {
    MultipleComponentsPreview(
        { ActionEntry(text = "Action without icon") {} },
        { ActionEntry(text = "Action without icon", isEnabled = false) {} },
        { ActionEntry(icon = { Image(imageVector = Icons.Default.Done, contentDescription = null, colorFilter = ColorFilter.tint(it)) }, text = "Action with icon") {} },
        { ActionEntry(isEnabled = false, icon = { Image(imageVector = Icons.Default.Done, contentDescription = null, colorFilter = ColorFilter.tint(it)) }, text = "Action with icon") {} },
        { CopyActionEntry {} },
        { CopyActionEntry(isEnabled = false) {} },
    )
}
