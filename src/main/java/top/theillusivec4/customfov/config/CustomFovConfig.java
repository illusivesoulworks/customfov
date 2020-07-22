/*
 * Copyright (c) 2018-2020 C4
 *
 * This file is part of Custom FoV, a mod made for Minecraft.
 *
 * Custom FoV is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Custom FoV is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Custom FoV.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.customfov.config;

public class CustomFovConfig {

  private static FovPermission fovPermission;

  private static double flyingModifier;
  private static double flyingMax;
  private static double flyingMin;

  private static double aimingModifier;
  private static double aimingMax;
  private static double aimingMin;

  private static double sprintingModifier;
  private static double sprintingMax;
  private static double sprintingMin;

  private static double effectsModifier;
  private static double effectsMax;
  private static double effectsMin;

  private static double underwaterModifier;
  private static double underwaterMax;
  private static double underwaterMin;

  public static FovPermission getFovPermission() {
    return fovPermission;
  }

  public static void setFovPermission(FovPermission fovPermission) {
    CustomFovConfig.fovPermission = fovPermission;
  }

  public static double getFlyingModifier() {
    return flyingModifier;
  }

  public static void setFlyingModifier(double flyingModifier) {
    CustomFovConfig.flyingModifier = flyingModifier;
  }

  public static double getFlyingMax() {
    return flyingMax;
  }

  public static void setFlyingMax(double flyingMax) {
    CustomFovConfig.flyingMax = flyingMax;
  }

  public static double getFlyingMin() {
    return flyingMin;
  }

  public static void setFlyingMin(double flyingMin) {
    CustomFovConfig.flyingMin = flyingMin;
  }

  public static double getAimingModifier() {
    return aimingModifier;
  }

  public static void setAimingModifier(double aimingModifier) {
    CustomFovConfig.aimingModifier = aimingModifier;
  }

  public static double getAimingMax() {
    return aimingMax;
  }

  public static void setAimingMax(double aimingMax) {
    CustomFovConfig.aimingMax = aimingMax;
  }

  public static double getAimingMin() {
    return aimingMin;
  }

  public static void setAimingMin(double aimingMin) {
    CustomFovConfig.aimingMin = aimingMin;
  }

  public static double getSprintingModifier() {
    return sprintingModifier;
  }

  public static void setSprintingModifier(double sprintingModifier) {
    CustomFovConfig.sprintingModifier = sprintingModifier;
  }

  public static double getSprintingMax() {
    return sprintingMax;
  }

  public static void setSprintingMax(double sprintingMax) {
    CustomFovConfig.sprintingMax = sprintingMax;
  }

  public static double getSprintingMin() {
    return sprintingMin;
  }

  public static void setSprintingMin(double sprintingMin) {
    CustomFovConfig.sprintingMin = sprintingMin;
  }

  public static double getEffectsModifier() {
    return effectsModifier;
  }

  public static void setEffectsModifier(double effectsModifier) {
    CustomFovConfig.effectsModifier = effectsModifier;
  }

  public static double getEffectsMax() {
    return effectsMax;
  }

  public static void setEffectsMax(double effectsMax) {
    CustomFovConfig.effectsMax = effectsMax;
  }

  public static double getEffectsMin() {
    return effectsMin;
  }

  public static void setEffectsMin(double effectsMin) {
    CustomFovConfig.effectsMin = effectsMin;
  }

  public static double getUnderwaterModifier() {
    return underwaterModifier;
  }

  public static void setUnderwaterModifier(double underwaterModifier) {
    CustomFovConfig.underwaterModifier = underwaterModifier;
  }

  public static double getUnderwaterMax() {
    return underwaterMax;
  }

  public static void setUnderwaterMax(double underwaterMax) {
    CustomFovConfig.underwaterMax = underwaterMax;
  }

  public static double getUnderwaterMin() {
    return underwaterMin;
  }

  public static void setUnderwaterMin(double underwaterMin) {
    CustomFovConfig.underwaterMin = underwaterMin;
  }

  public enum FovPermission {
    NONE, VANILLA, MODDED, ALL
  }
}
