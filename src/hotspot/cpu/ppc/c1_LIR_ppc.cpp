/*
 * Copyright (c) 2016, Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2016 SAP SE. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 *
 */

#include "precompiled.hpp"
#include "asm/register.hpp"
#include "c1/c1_LIR.hpp"

FloatRegister LIR_OprDesc::as_float_reg() const {
  return as_FloatRegister(fpu_regnr());
}

FloatRegister LIR_OprDesc::as_double_reg() const {
  return as_FloatRegister(fpu_regnrLo());
}

// Reg2 unused.
LIR_Opr LIR_OprFact::double_fpu(int reg1, int reg2) {
  assert(!as_FloatRegister(reg2)->is_valid(), "Not used on this platform");
  return (LIR_Opr)(intptr_t)((reg1 << LIR_OprDesc::reg1_shift) |
                             (reg1 << LIR_OprDesc::reg2_shift) |
                             LIR_OprDesc::double_type          |
                             LIR_OprDesc::fpu_register         |
                             LIR_OprDesc::double_size);
}

#ifndef PRODUCT
void LIR_Address::verify() const {
  assert(scale() == times_1, "Scaled addressing mode not available on PPC and should not be used");
  assert(disp() == 0 || index()->is_illegal(), "can't have both");
#ifdef _LP64
  assert(base()->is_cpu_register(), "wrong base operand");
  assert(index()->is_illegal() || index()->is_double_cpu(), "wrong index operand");
  assert(base()->type() == T_OBJECT || base()->type() == T_LONG || base()->type() == T_METADATA,
         "wrong type for addresses");
#else
  assert(base()->is_single_cpu(), "wrong base operand");
  assert(index()->is_illegal() || index()->is_single_cpu(), "wrong index operand");
  assert(base()->type() == T_OBJECT || base()->type() == T_INT || base()->type() == T_METADATA,
         "wrong type for addresses");
#endif
}
#endif // PRODUCT
