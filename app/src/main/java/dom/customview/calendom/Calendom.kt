package dom.customview.calendom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import dom.customview.calendom.databinding.ContainerCalendomBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Calendom @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var currentDate: LocalDateTime
    private var minDate: LocalDateTime
    private var maxDate: LocalDateTime
    private var showHeader: Boolean
    private var headerTextAppearanceId: Int
    private var headerFormat: String
    private var headerBackgroundId: Int
    private var monthBackwardButtonSrcId: Int
    private var monthForwardButtonSrcId: Int
    private var monthBackwardButtonBackgroundId: Int
    private var monthForwardButtonBackgroundId: Int
    private var headerChain: Int
    private var paddingHeaderPixelSize: Int


    lateinit var calendarAdapter: CalendarAdapter

    private val binding: ContainerCalendomBinding =
        ContainerCalendomBinding.inflate(LayoutInflater.from(context))

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.Calendom, defStyleAttr, defStyleRes)
            .apply {
                currentDate = getLocaleDateTime(getString(R.styleable.Calendom_currentDate))
                when (getInteger(R.styleable.Calendom_diffDateUnit, 0)) {
                    0 -> {
                        minDate = currentDate.minusYears(
                            getInteger(
                                R.styleable.Calendom_diffForMinDateFromCurrent,
                                100
                            ).toLong()
                        )
                        maxDate = currentDate.plusYears(
                            getInteger(
                                R.styleable.Calendom_diffForMaxDateFromCurrent,
                                100
                            ).toLong()
                        )
                    }

                    1 -> {
                        minDate = currentDate.minusMonths(
                            getInteger(
                                R.styleable.Calendom_diffForMinDateFromCurrent,
                                100
                            ).toLong()
                        )
                        maxDate = currentDate.plusMonths(
                            getInteger(
                                R.styleable.Calendom_diffForMaxDateFromCurrent,
                                100
                            ).toLong()
                        )
                    }

                    else -> {
                        minDate = currentDate.minusDays(
                            getInteger(
                                R.styleable.Calendom_diffForMinDateFromCurrent,
                                100
                            ).toLong()
                        )
                        maxDate = currentDate.plusDays(
                            getInteger(
                                R.styleable.Calendom_diffForMaxDateFromCurrent,
                                100
                            ).toLong()
                        )
                    }
                }
                showHeader = getBoolean(R.styleable.Calendom_showHeader, true)
                headerTextAppearanceId = getResourceId(
                    R.styleable.Calendom_headerTextAppearance,
                    androidx.appcompat.R.style.TextAppearance_AppCompat_Headline
                )
                headerFormat =
                    getText(R.styleable.Calendom_headerYearMonthTextFormat).toString().let {
                        it.ifBlank { "yyyy년 MM월" }
                    }
                headerBackgroundId = getResourceId(
                    R.styleable.Calendom_headerYearMonthBackground,
                    R.drawable.background_transparent
                )
                monthBackwardButtonSrcId = getResourceId(
                    R.styleable.Calendom_monthBackwardButtonSrc,
                    R.drawable.keyboard_arrow_left_24
                )
                monthForwardButtonSrcId = getResourceId(
                    R.styleable.Calendom_monthForwardButtonSrc,
                    R.drawable.keyboard_arrow_right_24
                )
                monthBackwardButtonBackgroundId = getResourceId(
                    R.styleable.Calendom_monthBackwardBackground,
                    R.drawable.background_transparent
                )
                monthForwardButtonBackgroundId = getResourceId(
                    R.styleable.Calendom_monthForwardBackground,
                    R.drawable.background_transparent
                )
                headerChain = getInteger(R.styleable.Calendom_headerChain, 0).let {
                    when(it) {
                        0 -> ConstraintSet.CHAIN_SPREAD_INSIDE
                        else -> ConstraintSet.CHAIN_PACKED
                    }
                }
                paddingHeaderPixelSize = getDimensionPixelSize(R.styleable.Calendom_paddingHeaderBottom, 0)
            }
        initViews()
    }

    private fun getLocaleDateTime(from: String?): LocalDateTime {
        return from?.let {
            if (it.length == 8) {
                var year = 0
                var month = 0
                var day = 0
                val datetime: LocalDateTime
                try {
                    year = it.substring(0, 4).toInt()
                    month = it.substring(4, 6).toInt()
                    day = it.substring(6).toInt()
                } catch (e: TypeCastException) {
                    e.printStackTrace()
                } finally {
                    datetime = if (year > 0 && month > 0 && day > 0)
                        LocalDate.of(year, month, day).atStartOfDay()
                    else LocalDate.now().atStartOfDay()
                }
                datetime
            } else LocalDate.now().atStartOfDay()
        } ?: LocalDate.now().atStartOfDay()
    }

    private fun initViews() {
        with(binding) {
            headerGroup.isVisible = showHeader
            if (headerGroup.isVisible) {
                headerCalendar.setTextAppearance(headerTextAppearanceId)
                headerCalendar.text = currentDate.format(DateTimeFormatter.ofPattern(headerFormat))
                headerCalendar.setBackgroundResource(headerBackgroundId)
                btnMonthBackward.setImageResource(monthBackwardButtonSrcId)
                btnMonthBackward.setBackgroundResource(monthBackwardButtonBackgroundId)
                btnMonthForward.setImageResource(monthForwardButtonSrcId)
                btnMonthForward.setBackgroundResource(monthForwardButtonBackgroundId)
                (btnMonthBackward.layoutParams as LayoutParams).horizontalChainStyle = headerChain
                marginHeader.layoutParams.height = paddingHeaderPixelSize
            }




            pagerCalendar.adapter
        }
    }

    private fun setAttrs() {

    }

    inner class CalendarAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

        override fun createFragment(position: Int): Fragment {
            TODO("Not yet implemented")
        }

    }
}