package cz.cvut.fel.tlappka.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fel.tlappka.R

/**
 * We need adapter which shows onboarding items corectly
 */
class OnboardingAdapter(val onboardingItems: List<OnboardingItem>) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        return OnboardingViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_container_onboarding, parent, false
        ));
    }

    override fun getItemCount(): Int {
        return onboardingItems.size;
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.setOnboardingData(onboardingItems.get(position));
    }

    inner class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitle: TextView = itemView.findViewById(R.id.textTitle);
        val textDescription: TextView = itemView.findViewById(R.id.textDescription);
        val imageOnboarding: ImageView = itemView.findViewById(R.id.imageOnboarding);

        fun setOnboardingData(onboardingItem : OnboardingItem) {
            textTitle.text = onboardingItem.title;
            textDescription.text = onboardingItem.description;
            imageOnboarding.setImageResource(onboardingItem.image);

        }
    }

}