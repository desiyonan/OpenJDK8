/*
 * Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved.
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
 */

#ifndef SHARE_GC_Z_ZALLOCATIONFLAGS_HPP
#define SHARE_GC_Z_ZALLOCATIONFLAGS_HPP

#include "gc/z/zBitField.hpp"
#include "memory/allocation.hpp"

//
// Allocation flags layout
// -----------------------
//
//   7   4 3 2 1 0
//  +---+-+-+-+-+-+
//  |000|1|1|1|1|1|
//  +---+-+-+-+-+-+
//  |   | | | | |
//  |   | | | | * 0-0 Java Thread Flag (1-bit)
//  |   | | | |
//  |   | | | * 1-1 Worker Thread Flag (1-bit)
//  |   | | |
//  |   | | * 2-2 Non-Blocking Flag (1-bit)
//  |   | |
//  |   | * 3-3 Relocation Flag (1-bit)
//  |   |
//  |   * 4-4 No Reserve Flag (1-bit)
//  |
//  * 7-5 Unused (3-bits)
//

class ZAllocationFlags {
private:
  typedef ZBitField<uint8_t, bool, 0, 1> field_java_thread;
  typedef ZBitField<uint8_t, bool, 1, 1> field_worker_thread;
  typedef ZBitField<uint8_t, bool, 2, 1> field_non_blocking;
  typedef ZBitField<uint8_t, bool, 3, 1> field_relocation;
  typedef ZBitField<uint8_t, bool, 4, 1> field_no_reserve;

  uint8_t _flags;

public:
  ZAllocationFlags() :
      _flags(0) {}

  void set_java_thread() {
    _flags |= field_java_thread::encode(true);
  }

  void set_worker_thread() {
    _flags |= field_worker_thread::encode(true);
  }

  void set_non_blocking() {
    _flags |= field_non_blocking::encode(true);
  }

  void set_relocation() {
    _flags |= field_relocation::encode(true);
  }

  void set_no_reserve() {
    _flags |= field_no_reserve::encode(true);
  }

  bool java_thread() const {
    return field_java_thread::decode(_flags);
  }

  bool worker_thread() const {
    return field_worker_thread::decode(_flags);
  }

  bool non_blocking() const {
    return field_non_blocking::decode(_flags);
  }

  bool relocation() const {
    return field_relocation::decode(_flags);
  }

  bool no_reserve() const {
    return field_no_reserve::decode(_flags);
  }
};

#endif // SHARE_GC_Z_ZALLOCATIONFLAGS_HPP
