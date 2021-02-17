package jds.projects.ddcompanion.my_classes.dd_classes

import jds.projects.ddcompanion.my_classes.dd_classes.DDAlignmentInfo.alg_cb
import jds.projects.ddcompanion.my_classes.dd_classes.DDAlignmentInfo.alg_cm
import jds.projects.ddcompanion.my_classes.dd_classes.DDAlignmentInfo.alg_cn
import jds.projects.ddcompanion.my_classes.dd_classes.DDAlignmentInfo.alg_lb
import jds.projects.ddcompanion.my_classes.dd_classes.DDAlignmentInfo.alg_lm
import jds.projects.ddcompanion.my_classes.dd_classes.DDAlignmentInfo.alg_ln
import jds.projects.ddcompanion.my_classes.dd_classes.DDAlignmentInfo.alg_nb
import jds.projects.ddcompanion.my_classes.dd_classes.DDAlignmentInfo.alg_nm
import jds.projects.ddcompanion.my_classes.dd_classes.DDAlignmentInfo.alg_nn

class DDAlignments(
    lb: Int,
    ln: Int,
    lm: Int,
    nb: Int,
    nn: Int,
    nm: Int,
    cb: Int,
    cn: Int,
    cm: Int
) {
    private var _alignments = mutableMapOf(
        Pair(alg_lb, 0),
        Pair(alg_ln, 0),
        Pair(alg_lm, 0),
        Pair(alg_nb, 0),
        Pair(alg_nn, 0),
        Pair(alg_nm, 0),
        Pair(alg_cb, 0),
        Pair(alg_cn, 0),
        Pair(alg_cm, 0)
    )

    val alignments: Map<Int, Int>
        get() = _alignments

    init {
        _alignments[alg_lb] = lb
        _alignments[alg_ln] = ln
        _alignments[alg_lm] = lm
        _alignments[alg_nb] = nb
        _alignments[alg_nn] = nn
        _alignments[alg_nm] = nm
        _alignments[alg_cb] = cb
        _alignments[alg_cn] = cn
        _alignments[alg_cm] = cm
    }
}