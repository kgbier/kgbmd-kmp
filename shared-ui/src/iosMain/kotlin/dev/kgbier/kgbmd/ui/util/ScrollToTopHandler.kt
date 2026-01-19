@file:OptIn(ExperimentalForeignApi::class)

package dev.kgbier.kgbmd.ui.util

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.launch
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.UIKit.UIScrollView
import platform.UIKit.UIScrollViewDelegateProtocol
import platform.darwin.NSObject

@Composable
actual fun ScrollToTopHandler(scrollState: ScrollableState) {
    val coroutineScope = rememberCoroutineScope()
    val scrollViewDelegate = remember(coroutineScope, scrollState) {
        createDelegate {
            coroutineScope.launch {
                when (scrollState) {
                    is ScrollState -> scrollState.animateScrollTo(0)
                    is LazyListState -> scrollState.animateScrollToItem(0)
                    is LazyGridState -> scrollState.animateScrollToItem(0)
                    is LazyStaggeredGridState -> scrollState.animateScrollToItem(0)
                    // Not animated, but a relatively safe fallback
                    else -> scrollState.scrollBy(Float.NEGATIVE_INFINITY)
                }
            }
        }
    }
    UIScrollView(scrollViewDelegate)
}

/**
 * Stolen from:
 * https://github.com/JetBrains/kotlinconf-app/blob/14a1ab61af971e901114cf83d4cf2d40f310fad0/shared/src/iosMain/kotlin/org/jetbrains/kotlinconf/ScrollToTopHandler.kt
 */

// Prerequisites for the scroll-to-top gesture to work are (https://developer.apple.com/documentation/uikit/uiscrollview/scrollstotop):
// * scrollsToTop property set to true
// * scrollViewShouldScrollToTop should return true
// * current position shouldn't be at 0

private fun createDelegate(
    onScrollToTop: () -> Unit,
): UIScrollViewDelegateProtocol {
    return object : NSObject(), UIScrollViewDelegateProtocol {
        override fun scrollViewShouldScrollToTop(scrollView: UIScrollView): Boolean = true

        override fun scrollViewDidScrollToTop(scrollView: UIScrollView) {
            scrollView.setContentOffset(CGPointMake(0.0, 100.0))
            scrollingToTop = false
        }

        private var firstScroll = true
        private var scrollingToTop = false
        private var lastKnownOffset = 1.0

        // Sync from Native to Compose
        override fun scrollViewDidScroll(scrollView: UIScrollView) {
            if (firstScroll) {
                firstScroll = false
                return
            }
            if (scrollingToTop) {
                return
            }

            val newOffset = scrollView.contentOffset.useContents { y }
            val scrollingUp = newOffset < lastKnownOffset
            lastKnownOffset = newOffset

            if (scrollingUp) {
                scrollingToTop = true
                onScrollToTop()
            }
        }
    }
}

@Composable
private fun UIScrollView(
    scrollViewDelegate: UIScrollViewDelegateProtocol,
) {
    UIKitView(
        factory = {
            UIScrollView(CGRectMake(0.0, 0.0, 100.0, 1.0)).apply {
                scrollsToTop = true
                delegate = scrollViewDelegate
                // without setting content size, scroll to top wouldn't work
                setContentSize(CGSizeMake(100.0, 10000.0))
                showsVerticalScrollIndicator = false
                showsHorizontalScrollIndicator = false
                setContentOffset(CGPointMake(0.0, 100.0)) // Enable scroll-to-top
            }
        },
        modifier = Modifier
            .alpha(0f)
            .height(1.dp)
            .fillMaxWidth()
    )
}
