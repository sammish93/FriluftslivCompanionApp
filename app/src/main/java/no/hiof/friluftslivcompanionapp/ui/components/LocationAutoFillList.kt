package no.hiof.friluftslivcompanionapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.hiof.friluftslivcompanionapp.viewmodels.UserViewModel

@Composable
fun LocationAutoFillList(
    viewModel: UserViewModel,
    onAddressSelected: (String) -> Unit
) {
    LazyColumn {
        items(viewModel.locationAutoFill) { result ->
            Text(
                text = result.address,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.fetchPlaceInfo(result.placeId)
                        onAddressSelected(result.address)
                    }
                    .padding(16.dp)
            )
        }
    }
}