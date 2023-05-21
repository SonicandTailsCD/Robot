// Made with Blockbench 4.2.3
// Exported for Minecraft version 1.7 - 1.12
// Paste this class into your mod and generate all required imports


public class ModelSimpleRobot extends ModelBase {
	private final ModelRenderer robotHead;
	private final ModelRenderer robotBody;
	private final ModelRenderer robotRightArm;
	private final ModelRenderer robotLeftArm;
	private final ModelRenderer robotRightLeg;
	private final ModelRenderer robotLeftLeg;
	private final ModelRenderer antenna;
	private final ModelRenderer robotHeadwear;
	private final ModelRenderer robotLeftArmwear;
	private final ModelRenderer robotRightArmwear;
	private final ModelRenderer robotLeftLegwear;
	private final ModelRenderer robotRightLegwear;
	private final ModelRenderer robotBodyWear;

	public ModelSimpleRobot() {
		textureWidth = 64;
		textureHeight = 32;

		robotHead = new ModelRenderer(this);
		robotHead.setRotationPoint(0.0F, 16.0F, 0.0F);
		

		robotBody = new ModelRenderer(this);
		robotBody.setRotationPoint(0.0F, 16.0F, 0.0F);
		

		robotRightArm = new ModelRenderer(this);
		robotRightArm.setRotationPoint(-4.0F, 17.0F, 0.0F);
		

		robotLeftArm = new ModelRenderer(this);
		robotLeftArm.setRotationPoint(4.0F, 17.0F, 0.0F);
		

		robotRightLeg = new ModelRenderer(this);
		robotRightLeg.setRotationPoint(-1.9F, 20.0F, 0.0F);
		

		robotLeftLeg = new ModelRenderer(this);
		robotLeftLeg.setRotationPoint(1.9F, 20.0F, 0.0F);
		

		antenna = new ModelRenderer(this);
		antenna.setRotationPoint(0.0F, 16.0F, 0.0F);
		

		robotHeadwear = new ModelRenderer(this);
		robotHeadwear.setRotationPoint(0.0F, 16.0F, 0.0F);
		robotHeadwear.cubeList.add(new ModelBox(robotHeadwear, 0, 0, -5.5F, -6.0F, -3.5F, 11, 6, 7, 0.5F, false));

		robotLeftArmwear = new ModelRenderer(this);
		robotLeftArmwear.setRotationPoint(4.0F, 17.0F, 0.0F);
		robotLeftArmwear.cubeList.add(new ModelBox(robotLeftArmwear, 26, 13, -0.5F, 0.0F, -0.5F, 1, 4, 1, 0.25F, true));

		robotRightArmwear = new ModelRenderer(this);
		robotRightArmwear.setRotationPoint(-4.0F, 17.0F, 0.0F);
		robotRightArmwear.cubeList.add(new ModelBox(robotRightArmwear, 26, 13, -0.5F, 0.0F, -0.5F, 1, 4, 1, 0.25F, false));

		robotLeftLegwear = new ModelRenderer(this);
		robotLeftLegwear.setRotationPoint(1.9F, 20.0F, 0.0F);
		robotLeftLegwear.cubeList.add(new ModelBox(robotLeftLegwear, 0, 13, -1.0F, 0.0F, -0.5F, 2, 4, 1, 0.25F, true));

		robotRightLegwear = new ModelRenderer(this);
		robotRightLegwear.setRotationPoint(-1.9F, 20.0F, 0.0F);
		robotRightLegwear.cubeList.add(new ModelBox(robotRightLegwear, 0, 13, -1.0F, 0.0F, -0.5F, 2, 4, 1, 0.25F, false));

		robotBodyWear = new ModelRenderer(this);
		robotBodyWear.setRotationPoint(0.0F, 16.0F, 0.0F);
		robotBodyWear.cubeList.add(new ModelBox(robotBodyWear, 6, 13, -3.5F, 0.0F, -1.5F, 7, 5, 3, 0.25F, false));
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		robotHead.render(f5);
		robotBody.render(f5);
		robotRightArm.render(f5);
		robotLeftArm.render(f5);
		robotRightLeg.render(f5);
		robotLeftLeg.render(f5);
		antenna.render(f5);
		robotHeadwear.render(f5);
		robotLeftArmwear.render(f5);
		robotRightArmwear.render(f5);
		robotLeftLegwear.render(f5);
		robotRightLegwear.render(f5);
		robotBodyWear.render(f5);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}