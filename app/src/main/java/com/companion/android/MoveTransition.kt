package com.companion.android

import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.ChangeTransform
import androidx.transition.TransitionSet

class MoveTransition: TransitionSet {
    constructor() {
        ordering = ORDERING_TOGETHER
        addTransition(ChangeBounds()).
            addTransition(ChangeTransform()).
            addTransition(ChangeImageTransform())
    }
}